package me.blog.ariticle;

public record Article(
    Integer index,
    String title,
    Integer categoryId,
    String path
) {
}
