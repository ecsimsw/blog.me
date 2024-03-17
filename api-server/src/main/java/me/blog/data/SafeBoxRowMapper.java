package me.blog.data;

import me.blog.domain.SafeBox;

public class SafeBoxRowMapper {

    private static final int INDEX = 1;
    private static final int INDEX_OF_ARTICLE_ID = 2;

    public static SafeBox toEntity(String line) {
        var lineEntities = line.split("\\|");
        return new SafeBox(
            Integer.parseInt(lineEntities[INDEX]),
            Integer.parseInt(lineEntities[INDEX_OF_ARTICLE_ID])
        );
    }
}
