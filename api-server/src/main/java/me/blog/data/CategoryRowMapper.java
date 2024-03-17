package me.blog.data;

import me.blog.domain.Category;

public class CategoryRowMapper {

    private static final int INDEX = 1;
    private static final int INDEX_OF_TITlE = 2;

    public static Category toEntity(String line) {
        var lineEntities = line.split("\\|");
        return new Category(
            Integer.parseInt(lineEntities[INDEX]),
            lineEntities[INDEX_OF_TITlE]
        );
    }
}
