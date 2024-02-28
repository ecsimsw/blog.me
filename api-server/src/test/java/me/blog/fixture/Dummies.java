package me.blog.fixture;

import java.util.List;
import me.blog.domain.Article;
import me.blog.domain.Category;

public class Dummies {

    public static final List<Article> MOCK_ARTICLE_DATA = List.of(
        new Article(1, "A", 1, "A.html"),
        new Article(2, "B", 1, "B.html"),
        new Article(3, "C", 1, "C.html"),
        new Article(4, "D", 1, "D.html"),
        new Article(5, "F", 2, "F.html"),
        new Article(6, "G", 2, "G.html")
    );

    public static final List<Category> MOCK_CATEGORY_DATA = List.of(
        new Category(1, "A"),
        new Category(2, "B"),
        new Category(3, "C"),
        new Category(4, "D"),
        new Category(5, "E")
    );
}
