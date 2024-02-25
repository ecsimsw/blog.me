package me.blog.ariticle;

import me.blog.data.ArticleDao;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class Articles {

    private final List<Article> articles;

    public Articles(ArticleDao articleDao) {
        var articles = articleDao.readDataFile();
        if (articles.isEmpty()) {
            throw new NoSuchElementException("File data is empty");
        }
        this.articles = articles;
    }

    public List<Article> search(String title, int pageSize, int pageNumber) {
        var collect = articles.stream()
            .filter(it -> it.title().contains(title))
            .limit(pageNumber * pageSize + pageSize)
            .collect(Collectors.toList());
        return collect.subList(
            Math.min(collect.size(), pageNumber * pageSize),
            Math.min(pageNumber * pageSize + pageSize, collect.size())
        );
    }

    public Article getById(int id) {
        return articles.stream()
            .filter(it -> it.index().equals(id))
            .findAny()
            .orElseThrow(() -> new NoSuchElementException("Not exists id"));
    }

    public List<Article> findAllByTitle(String title) {
        return articles.stream()
            .filter(it -> it.title().equals(title))
            .collect(Collectors.toList());
    }

    public List<Article> findAllByTitleContains(String title) {
        return articles.stream()
            .filter(it -> it.title().contains(title))
            .collect(Collectors.toList());
    }
}
