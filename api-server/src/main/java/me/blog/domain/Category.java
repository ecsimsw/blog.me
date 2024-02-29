package me.blog.domain;

public record Category(
    int id,
    String name
) implements Comparable<Category> {

    @Override
    public int compareTo(Category o) {
        return this.id - o.id;
    }
}
