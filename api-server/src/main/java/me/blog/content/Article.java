package me.blog.content;

public record Article(
    int index,
    String title,
    int categoryId,
    String path
) implements Comparable<Article> {

    @Override
    public int compareTo(Article o) {
        return this.index - o.index;
    }
}
