package me.blog.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class Articles {

    private final List<Article> articles;

    public Articles(List<Article> articles) {
        if (articles.isEmpty()) {
            throw new NoSuchElementException("File data is empty");
        }
        this.articles = new ArrayList<>(articles);
    }

    public Optional<Article> findById(int id) {
        return articles.stream()
            .filter(it -> it.id() == id)
            .findAny();
    }

    public List<Article> findAllOrderByIndexDesc(int pageSize, int pageNumber) {
        var collect = articles.stream()
            .limit((long) pageNumber * pageSize + pageSize)
            .collect(Collectors.toList());
        return collect.subList(
            Math.min(collect.size(), pageNumber * pageSize),
            Math.min(collect.size(), pageNumber * pageSize + pageSize)
        );
    }

    public List<Article> findAllByCategoryOrderByIndexDesc(int categoryId, int pageSize, int pageNumber) {
        var collect = articles.stream()
            .filter(it -> it.categoryId() == categoryId)
            .limit((long) pageNumber * pageSize + pageSize)
            .collect(Collectors.toList());
        return collect.subList(
            Math.min(collect.size(), pageNumber * pageSize),
            Math.min(collect.size(), pageNumber * pageSize + pageSize)
        );
    }

    public List<Article> findAllTitleContainsOrderByIndexDesc(String keyword, int pageSize, int pageNumber) {
        var collect = articles.stream()
            .filter(it -> it.title().contains(keyword))
            .limit((long) pageNumber * pageSize + pageSize)
            .collect(Collectors.toList());
        return collect.subList(
            Math.min(collect.size(), pageNumber * pageSize),
            Math.min(collect.size(), pageNumber * pageSize + pageSize)
        );
    }

    public int countByCategory(int category) {
        return (int) articles.stream()
            .filter(it -> it.categoryId() == category)
            .count();
    }

    public int count() {
        return articles.size();
    }
}
