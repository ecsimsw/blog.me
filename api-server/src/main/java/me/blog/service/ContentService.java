package me.blog.service;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import me.blog.domain.Article;
import me.blog.domain.ArticleRepository;
import me.blog.domain.CategoryRepository;
import me.blog.dto.CategoryResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ContentService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    public Optional<Article> findById(int id) {
        return articleRepository.findById(id);
    }

    public List<Article> search(String keyword, Pageable pageable) {
        var pageSize = pageable.getPageSize();
        var pageNumber = pageable.getPageNumber();
        return articleRepository.findAllTitleContainsOrderByIndexDesc(keyword, pageSize, pageNumber);
    }

    public List<Article> articlesInCategory(Optional<String> optCategory, Pageable pageable) {
        var pageSize = pageable.getPageSize();
        var pageNumber = pageable.getPageNumber();
        if (optCategory.isEmpty()) {
            return articleRepository.findAllOrderByIndexDesc(pageSize, pageNumber);
        }
        var categoryName = optCategory.orElseThrow();
        var category = categoryRepository.getByName(categoryName);
        return articleRepository.findAllByCategoryOrderByIndexDesc(category.id(), pageSize, pageNumber);
    }

    public int countArticleIn(Optional<String> optCategory) {
        if (optCategory.isEmpty()) {
            return articleRepository.count();
        }
        var category = categoryRepository.getByName(optCategory.orElseThrow());
        return articleRepository.countByCategory(category.id());
    }

    public List<CategoryResponse> categories() {
        return categoryRepository.findAll().stream()
            .map(it -> CategoryResponse.of(it, articleRepository.countByCategory(it.id())))
            .collect(Collectors.toList());
    }

    public String getPathById(int id) {
        return articleRepository.findById(id)
            .orElseThrow(()-> new NoSuchElementException("No such article id with " + id))
            .path();
    }
}
