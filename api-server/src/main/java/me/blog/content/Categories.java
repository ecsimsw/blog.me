package me.blog.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import me.blog.data.CategoryDao;
import org.springframework.stereotype.Component;

public class Categories {

    private final List<Category> categories;

    public Categories(List<Category> categories) {
        if (categories.isEmpty()) {
            throw new NoSuchElementException("File data is empty");
        }
        this.categories = new ArrayList<>(categories);
        Collections.sort(this.categories);
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
