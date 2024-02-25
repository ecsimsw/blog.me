package me.blog.data;

import me.blog.ariticle.Article;

public class ArticleRowMapper {

    private static final int INDEX = 1;
    private static final int INDEX_OF_TITlE = 2;
    private static final int INDEX_OF_CATEGORY_ID = 3;
    private static final int INDEX_OF_PATH = 4;

    public static Article toArticle(String[] line) {
        return new Article(
            Integer.parseInt(line[INDEX]),
            line[INDEX_OF_TITlE],
            Integer.parseInt(line[INDEX_OF_CATEGORY_ID]),
            line[INDEX_OF_PATH]
        );
    }
}
