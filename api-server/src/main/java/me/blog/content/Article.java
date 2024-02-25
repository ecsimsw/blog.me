package me.blog.content;

public record Article(
    int index,
    String title,
    int categoryId,
    String path
) {
}
