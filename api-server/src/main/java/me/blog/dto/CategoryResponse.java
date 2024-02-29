package me.blog.dto;

import me.blog.domain.Category;

public record CategoryResponse(
    int id,
    String name,
    int numberOfPosts
) {
    public static CategoryResponse of(Category category, int numberOfPosts) {
        return new CategoryResponse(category.id(), category.name(), numberOfPosts);
    }
}
