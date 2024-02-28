package me.blog.content;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticlesTest {

    private static final List<Article> MOCK_ARTICLE_DATA = List.of(
        new Article(1, "A", 1, "A.html"),
        new Article(2, "B", 1, "B.html"),
        new Article(3, "C", 1, "C.html"),
        new Article(4, "D", 1, "D.html"),
        new Article(5, "F", 2, "F.html"),
        new Article(6, "G", 2, "G.html")
    );

    private Articles articles;

    @BeforeEach
    public void initArticles() {
        this.articles = new Articles(MOCK_ARTICLE_DATA);
    }

    @DisplayName("Id 로 글을 조회한다.")
    @Test
    void getById() {
        var index = 1;
        var searched = articles.getById(index);
        var expected = MOCK_ARTICLE_DATA.get(0);
        assertEquals(expected, searched);
    }

    @DisplayName("페이지 정보에 해당하는 글 목록을 반환한다.")
    @Test
    void findAll() {
        var pageSize = 2;
        var pageNumber = 2;
        var searched = articles.findAllOrderByIndexDesc(pageSize, pageNumber);
        var expected = List.of(MOCK_ARTICLE_DATA.get(1), MOCK_ARTICLE_DATA.get(0));
        assertEquals(expected, searched);
    }

    @DisplayName("카테고리에 포함되는 글 목록을 반환한다.")
    @Test
    void findAllByCategory() {
        var pageSize = MOCK_ARTICLE_DATA.size();
        var pageNumber = 0;
        var searched = articles.findAllByCategoryOrderByIndexDesc(2, pageSize, pageNumber);
        var expected =
            List.of(MOCK_ARTICLE_DATA.get(5), MOCK_ARTICLE_DATA.get(4));
        assertEquals(expected, searched);
    }

    @DisplayName("제목에 검색 키워드가 포함되는 글 목록을 반환한다.")
    @Test
    void findAllByTitleContains() {
        var searchKeyword = "A";
        var pageSize = MOCK_ARTICLE_DATA.size();
        var pageNumber = 0;
        var searched = articles.findAllTitleContainsOrderByIndexDesc(searchKeyword, pageSize, pageNumber);
        var expected = List.of(MOCK_ARTICLE_DATA.get(0));
        assertEquals(expected, searched);
    }

    @DisplayName("카테고리에 포함되는 글 개수를 반환한다.")
    @Test
    void countByCategory() {
        var categoryId = 1;
        var searched = articles.countByCategory(categoryId);
        var expected = MOCK_ARTICLE_DATA.stream().filter(it -> it.categoryId() == categoryId).count();
        assertEquals(expected, searched);
    }

    @DisplayName("전체 글 개수를 반환한다.")
    @Test
    void count() {
        var searched = articles.count();
        var expected = MOCK_ARTICLE_DATA.size();
        assertEquals(expected, searched);
    }
}