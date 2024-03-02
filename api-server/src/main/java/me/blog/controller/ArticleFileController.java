package me.blog.controller;

import me.blog.service.ContentService;
import me.blog.service.DailyCountCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ArticleFileController {

    private final ContentService contentService;
    private final DailyCountCacheService cacheService;
    private final String fileRootDirectory;

    public ArticleFileController(
        ContentService contentService,
        DailyCountCacheService cacheService,
        @Value("${html.article.file.path}") String fileRootDirectory
    ) {
        this.contentService = contentService;
        this.cacheService = cacheService;
        this.fileRootDirectory = fileRootDirectory;
    }

    @GetMapping("/api/article/{id}")
    public String serveArticleFile(@PathVariable int id) {
        // TODO :: AUTH
        cacheService.count(id, 1);
        var filePath = contentService.getPathById(id);
        return "forward:/" + fileRootDirectory + filePath;
    }
}
