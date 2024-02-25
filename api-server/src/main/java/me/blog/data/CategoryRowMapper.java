package me.blog.data;

import me.blog.content.Category;

public class CategoryRowMapper {

    private static final int INDEX = 1;
    private static final int INDEX_OF_TITlE = 2;

    public static Category toEntity(String[] line) {
        return new Category(
            Integer.parseInt(line[INDEX]),
            line[INDEX_OF_TITlE]
        );
    }
}
