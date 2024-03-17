package me.blog.controller;

import java.util.HashMap;
import java.util.Map;
import me.blog.service.ContentService;
import me.blog.service.DailyCountCacheService;
import me.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ArticleFileController {

    private final ContentService contentService;
    private final DailyCountCacheService countService;
    private final String fileRootDirectory;

    public ArticleFileController(
        ContentService contentService,
        DailyCountCacheService countService,
        @Value("${html.article.file.path}") String fileRootDirectory
    ) {
        this.contentService = contentService;
        this.countService = countService;
        this.fileRootDirectory = fileRootDirectory;
    }

    @GetMapping("/api/test")
    public String serveArticleFile() {
        // TODO :: AUTH
        String token = JwtUtils.createToken(
            "hadsfdasfadsfsdafadsfadsfdsafdsafdsafdasfsdafdsafasdfdsafsadfi",
            Map.of("user", "hi"),
            1
        );
        JwtUtils.validate("hadsfdasfadsfsdafadsfadsfdsafdsafdsafdasfsdafdsafasdfdsafsadfi", token);

//        System.out.println(token);
        return token;

//        countService.count(id, 1);
//        var filePath = contentService.getPathById(id);
//        return "forward:/" + fileRootDirectory + filePath;
    }
}
