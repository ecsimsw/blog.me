package me.blog.data;

import me.blog.ariticle.Article;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ArticleDao {

    private final File file;

    public ArticleDao(@Value("${data.file.path}") String dataFilePath) {
        file = new File(dataFilePath);
    }

    public List<Article> readDataFile() {
        try {
            return Files.readLines(file, Charsets.UTF_8).stream()
                .skip(1)
                .filter(it-> !it.isBlank())
                .map(it -> ArticleRowMapper.toArticle(it.split("\\|")))
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load data file", e);
        }
    }
}

