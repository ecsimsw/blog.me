package me.blog.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class CategoryRepository {

    private final List<Category> categories;

    public CategoryRepository(List<Category> categories) {
        if (categories.isEmpty()) {
            throw new NoSuchElementException("File data is empty");
        }
        this.categories = new ArrayList<>(categories);
    }

    public List<Category> findAll() {
        return new ArrayList<>(categories);
    }

    public Category getByName(String categoryName) {
        return categories.stream()
            .filter(it -> it.name().equals(categoryName))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("No category name with " + categoryName));
    }

    public int size() {
        return categories.size();
    }
}
