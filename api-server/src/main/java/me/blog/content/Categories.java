package me.blog.content;

import java.util.Optional;
import me.blog.data.CategoryDao;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class Categories {

    private final List<Category> categories;

    public Categories(CategoryDao categoryDao) {
        var categories = categoryDao.readDataFile();
        if (categories.isEmpty()) {
            throw new NoSuchElementException("File data is empty");
        }
        Collections.sort(categories);
        this.categories = categories;
    }

    public List<Category> findAll() {
        return categories;
    }

    public Category getByName(String categoryName) {
        return categories.stream()
            .filter(it -> it.name().equals(categoryName))
            .findFirst()
            .orElseThrow(()-> new NoSuchElementException("No category name with " + categoryName));
    }
}
