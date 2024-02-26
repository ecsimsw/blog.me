package me.blog.content;

import java.util.Collections;
import java.util.Comparator;
import me.blog.data.ArticleDao;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class Articles {

    private final List<Article> articles;

    public Articles(ArticleDao articleDao) {
        var articles = articleDao.readDataFile();
        if (articles.isEmpty()) {
            throw new NoSuchElementException("File data is empty");
        }
        Collections.sort(articles, Comparator.reverseOrder());
        this.articles = articles;
    }

    public Article getById(int id) {
        return articles.stream()
            .filter(it -> it.index() == id)
            .findAny()
            .orElseThrow(() -> new NoSuchElementException("Not exists id"));
    }

    public List<Article> findAll(int pageSize, int pageNumber) {
        var collect = articles.stream()
            .limit(pageNumber * pageSize + pageSize)
            .collect(Collectors.toList());
        return collect.subList(
            Math.min(collect.size(), pageNumber * pageSize),
            Math.min(collect.size(), pageNumber * pageSize + pageSize)
        );
    }

    public List<Article> findAllByCategory(int categoryId, int pageSize, int pageNumber) {
        var collect = articles.stream()
            .filter(it -> it.categoryId() == categoryId)
            .limit(pageNumber * pageSize + pageSize)
            .collect(Collectors.toList());
        return collect.subList(
            Math.min(collect.size(), pageNumber * pageSize),
            Math.min(collect.size(), pageNumber * pageSize + pageSize)
        );
    }

    public List<Article> findAllByTitleContains(String keyword, int pageSize, int pageNumber) {
        var collect = articles.stream()
            .filter(it -> it.title().contains(keyword))
            .limit(pageNumber * pageSize + pageSize)
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
