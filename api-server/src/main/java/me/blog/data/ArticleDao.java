package me.blog.data;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import me.blog.domain.Article;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ArticleDao {

    private final File file;

    public ArticleDao(@Value("${data.article.file.path}") String dataFilePath) {
        file = new File(dataFilePath);
    }

    public List<Article> readDataFile() {
        try {
            return Files.readLines(file, Charsets.UTF_8).stream()
                .skip(1)
                .filter(it -> !it.isBlank())
                .map(it -> {
                    log.info("read article data :" + it);
                    return ArticleRowMapper.toEntity(it.split("\\|"));
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load data file", e);
        }
    }
}

