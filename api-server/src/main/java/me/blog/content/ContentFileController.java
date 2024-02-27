package me.blog.content;

import me.blog.count.DailyCountCacheService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ContentFileController {

    private final ContentService contentService;
    private final DailyCountCacheService cacheService;

    public ContentFileController(ContentService contentService, DailyCountCacheService cacheService) {
        this.contentService = contentService;
        this.cacheService = cacheService;
    }

    @GetMapping("/article/{id}")
    public String serveArticleFile(@PathVariable int id) {
        cacheService.count(id, 1);
        var filePath = contentService.getPathById(id);
        return "forward:/" + filePath;
    }
}
