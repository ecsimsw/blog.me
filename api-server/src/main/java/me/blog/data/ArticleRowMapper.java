package me.blog.data;

import me.blog.domain.Article;

public class ArticleRowMapper {

    private static final int INDEX = 1;
    private static final int INDEX_OF_CATEGORY_ID = 2;
    private static final int INDEX_OF_TITlE = 3;
    private static final int INDEX_OF_PATH = 4;

    public static Article toEntity(String line) {
        var lineEntities = line.split("\\|");
        return new Article(
            Integer.parseInt(lineEntities[INDEX]),
            lineEntities[INDEX_OF_TITlE],
            Integer.parseInt(lineEntities[INDEX_OF_CATEGORY_ID]),
            lineEntities[INDEX_OF_PATH]
        );
    }
}
