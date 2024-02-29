package me.blog.domain;

public record Article(
    int id,
    String title,
    int categoryId,
    String path
) implements Comparable<Article> {

    @Override
    public int compareTo(Article o) {
        return this.id - o.id;
    }
}
