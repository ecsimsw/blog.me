package me.blog.content;

public record Category (
   int index,
   String name
) implements Comparable<Category> {

    @Override
    public int compareTo(Category o) {
        return this.index - o.index;
    }
}

// TODO :: feat ORDER

