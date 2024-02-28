package me.blog.service;

import static me.blog.fixture.Dummies.MOCK_ARTICLE_DATA;
import static me.blog.fixture.Dummies.MOCK_CATEGORY_DATA;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import me.blog.domain.Articles;
import me.blog.domain.Categories;
import me.blog.dto.CategoryResponse;
import me.blog.service.ContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

class ContentServiceTest {

    private Articles articles;
    private Categories categories;
    private ContentService contentService;

    @BeforeEach
    public void initService() {
        this.articles = new Articles(MOCK_ARTICLE_DATA);
        this.categories = new Categories(MOCK_CATEGORY_DATA);
        this.contentService = new ContentService(articles, categories);
    }

    @DisplayName("글 제목으로 검색할 수 있다.")
    @Test
    void search() {
        var pageNumber = 0;
        var pageSize = 2;
        var searchKeyword = "A";
        var result = contentService.search(searchKeyword, PageRequest.of(pageNumber, pageSize));
        var expected = articles.findAllTitleContainsOrderByIndexDesc(searchKeyword, pageSize, pageNumber);
        assertEquals(expected, result);
    }

    @DisplayName("카테고리에 포함된 글을 검색할 수 있다.")
    @Test
    void articlesInCategory() {
        var pageNumber = 0;
        var pageSize = 2;
        var categoryOpt = Optional.of("B");
        var result = contentService.articlesInCategory(categoryOpt, PageRequest.of(pageNumber, pageSize));

        var category = categories.getByName(categoryOpt.orElseThrow());
        var expected = articles.findAllByCategoryOrderByIndexDesc(category.index(), pageSize, pageNumber);
        assertEquals(expected, result);
    }

    @DisplayName("검색에 카테고리 정보가 없다면 전체 글 목록에서 조회한다.")
    @Test
    void articlesWithoutCategory() {
        var pageNumber = 0;
        var pageSize = 2;
        var result = contentService.articlesInCategory(Optional.empty(), PageRequest.of(pageNumber, pageSize));
        var expected = articles.findAllOrderByIndexDesc(pageSize, pageNumber);
        assertEquals(expected, result);
    }

    @DisplayName("존재하지 않는 카테고리에 포함된 글 목록을 조회할 경우 예외를 반환한다.")
    @Test
    void searchArticleInNotExistsCategory() {
        var pageNumber = 0;
        var pageSize = 2;
        assertThatThrownBy(
            () -> contentService.articlesInCategory(Optional.of("Z"), PageRequest.of(pageNumber, pageSize))
        ).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("카테고리에 포함된 글 개수를 조회한다.")
    @Test
    void countArticleIn() {
        var optCategory = Optional.of("B");
        var result = contentService.countArticleIn(optCategory);

        var category = categories.getByName(optCategory.orElseThrow());
        var expected = articles.countByCategory(category.index());
        assertEquals(expected, result);
    }

    @DisplayName("존재하지 않는 카테고리에 포함된 글 개수를 조회할 경우 예외를 반환한다.")
    @Test
    void countArticleInNotExistsCategory() {
        assertThatThrownBy(
            () -> contentService.countArticleIn(Optional.of("Z"))
        ).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("카테고리별 포함된 글 개수가 포함 카테고리 목록 정보를 반환한다.")
    @Test
    void categories() {
        var result = contentService.categories();
        var expectedCategories = categories.findAll().stream()
            .map(it -> new CategoryResponse(
                it.index(),
                it.name(),
                articles.countByCategory(it.index()))
            ).collect(Collectors.toList());
        assertEquals(result, expectedCategories);
    }

    @DisplayName("글 id 에 해당하는 path 를 반환한다.")
    @Test
    void getPathById() {
        var articleId = 1;
        var result = contentService.getPathById(articleId);
        var expected = articles.getById(articleId).path();
        assertEquals(expected, result);
    }
}