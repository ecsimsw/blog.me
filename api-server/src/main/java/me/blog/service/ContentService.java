package me.blog.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.blog.domain.Article;
import me.blog.domain.Articles;
import me.blog.domain.Categories;
import me.blog.dto.CategoryResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContentService {

    private final Articles articles;
    private final Categories categories;

    public Article findById(int id) {
        return articles.getById(id);
    }

    public List<Article> search(String keyword, Pageable pageable) {
        var pageSize = pageable.getPageSize();
        var pageNumber = pageable.getPageNumber();
        return articles.findAllTitleContainsOrderByIndexDesc(keyword, pageSize, pageNumber);
    }

    public List<Article> articlesInCategory(Optional<String> optCategory, Pageable pageable) {
        var pageSize = pageable.getPageSize();
        var pageNumber = pageable.getPageNumber();
        if(optCategory.isEmpty()) {
            return articles.findAllOrderByIndexDesc(pageSize, pageNumber);
        }
        var categoryName = optCategory.orElseThrow();
        var category = categories.getByName(categoryName);
        return articles.findAllByCategoryOrderByIndexDesc(category.index(), pageSize, pageNumber);
    }

    public int countArticleIn(Optional<String> optCategory) {
        if(optCategory.isEmpty()) {
            return articles.count();
        }
        var category = categories.getByName(optCategory.orElseThrow());
        return articles.countByCategory(category.index());
    }

    public List<CategoryResponse> categories() {
        return categories.findAll().stream()
            .map(it -> CategoryResponse.of(it, articles.countByCategory(it.index())))
            .collect(Collectors.toList());
    }

    public Object getPathById(int id) {
        return articles.getById(id).path();
    }
}
