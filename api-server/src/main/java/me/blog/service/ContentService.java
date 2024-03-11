package me.blog.service;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import me.blog.domain.Article;
import me.blog.domain.Articles;
import me.blog.domain.Categories;
import me.blog.dto.CategoryResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ContentService {

    private final Articles articles;
    private final Categories categories;

    public Optional<Article> findById(int id) {
        return articles.findById(id);
    }

    public List<Article> search(String keyword, Pageable pageable) {
        var pageSize = pageable.getPageSize();
        var pageNumber = pageable.getPageNumber();
        return articles.findAllTitleContainsOrderByIndexDesc(keyword, pageSize, pageNumber);
    }

    public List<Article> articlesInCategory(Optional<String> optCategory, Pageable pageable) {
        var pageSize = pageable.getPageSize();
        var pageNumber = pageable.getPageNumber();
        if (optCategory.isEmpty()) {
            return articles.findAllOrderByIndexDesc(pageSize, pageNumber);
        }
        var categoryName = optCategory.orElseThrow();
        var category = categories.getByName(categoryName);
        return articles.findAllByCategoryOrderByIndexDesc(category.id(), pageSize, pageNumber);
    }

    public int countArticleIn(Optional<String> optCategory) {
        if (optCategory.isEmpty()) {
            return articles.count();
        }
        var category = categories.getByName(optCategory.orElseThrow());
        return articles.countByCategory(category.id());
    }

    public List<CategoryResponse> categories() {
        return categories.findAll().stream()
            .map(it -> CategoryResponse.of(it, articles.countByCategory(it.id())))
            .collect(Collectors.toList());
    }

    public String getPathById(int id) {
        return articles.findById(id)
            .orElseThrow(()-> new NoSuchElementException("No such article id with " + id))
            .path();
    }
}
