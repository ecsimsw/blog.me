package me.blog.controller;

import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import me.blog.service.AuthService;
import me.blog.service.ContentService;
import me.blog.service.DailyCountCacheService;
import me.blog.utils.AuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleFileController {

    private final ContentService contentService;
    private final DailyCountCacheService countService;
    private final AuthService authService;
    private final String fileRootDirectory;

    public ArticleFileController(
        ContentService contentService,
        DailyCountCacheService countService,
        AuthService authService,
        @Value("${html.article.file.path}") String fileRootDirectory
    ) {
        this.contentService = contentService;
        this.countService = countService;
        this.authService = authService;
        this.fileRootDirectory = fileRootDirectory;
    }

    @GetMapping("/api/article/{id}")
    public String serveArticleFile(
        @AuthToken(tokenKey = "ecsimsw-blog-token") Optional<String> authToken,
        @PathVariable int id
    ) {
        countService.count(id, 1);
        authService.validateAccess(id, authToken);
        var filePath = contentService.getPathById(id);
        return "forward:/" + fileRootDirectory + filePath;
    }

    @PostMapping("/api/login")
    public String requireToken(
        @RequestBody String password,
        @RequestParam(required = false) Optional<Integer> articleId,
        HttpServletResponse response
    ) {
        authService.authenticate(password, response);
        if(articleId.isEmpty()) {
            return "redirect:/";
        } else {
            return "redirect:/api/article/" + articleId.orElseThrow();
        }
    }
}
