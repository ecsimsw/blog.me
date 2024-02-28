package me.blog.controller;

import me.blog.service.ContentService;
import me.blog.service.DailyCountCacheService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ArticleFileController {

    private final ContentService contentService;
    private final DailyCountCacheService cacheService;

    public ArticleFileController(ContentService contentService, DailyCountCacheService cacheService) {
        this.contentService = contentService;
        this.cacheService = cacheService;
    }

    @GetMapping("/article/{id}")
    public String serveArticleFile(@PathVariable int id) {
        // TODO :: AUTH
        cacheService.count(id, 1);
        var filePath = contentService.getPathById(id);
        return "forward:/" + filePath;
    }
}
