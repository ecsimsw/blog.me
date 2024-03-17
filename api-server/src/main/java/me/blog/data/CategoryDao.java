package me.blog.data;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import me.blog.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryDao {

    private final File file;

    public CategoryDao(
        @Autowired ResourceLoader resourceLoader,
        @Value("${data.category.file.path}") String dataFilePath
    ) throws IOException {
        this.file = resourceLoader.getResource("classpath:" + dataFilePath).getFile();
    }

    public List<Category> readDataFile() {
        try {
            return Files.readLines(file, Charsets.UTF_8).stream()
                .skip(1)
                .filter(it -> !it.isBlank())
                .map(CategoryRowMapper::toEntity)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load data file", e);
        }
    }
}

