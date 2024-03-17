package me.blog.data;

import static me.blog.config.DataConfig.DATA_SAFEBOX_FILE_PATH;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.blog.domain.Article;
import me.blog.domain.SafeBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class SafeBoxDao {

    private final File file;

    public SafeBoxDao(ResourceLoader resourceLoader) throws IOException {
        this.file = resourceLoader.getResource("classpath:" + DATA_SAFEBOX_FILE_PATH).getFile();
    }

    public List<SafeBox> readDataFile() {
        try {
            return Files.readLines(file, Charsets.UTF_8).stream()
                .skip(1)
                .filter(it -> !it.isBlank())
                .map(SafeBoxRowMapper::toEntity)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load data file", e);
        }
    }
}

