package me.blog.controller;

import static me.blog.config.FilePathVariables.ARTICLE_FILE_ROOT_PATH;
import static me.blog.config.AuthConfig.TOKEN_COOKIE_KEY;

import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.blog.service.AuthService;
import me.blog.service.ContentService;
import me.blog.service.DailyCountCacheService;
import me.blog.utils.AuthToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class ArticleFileController {

    private final ContentService contentService;
    private final DailyCountCacheService countService;
    private final AuthService authService;

    @GetMapping("/api/article/{id}")
    public String serveArticleFile(
        @AuthToken(tokenKey = TOKEN_COOKIE_KEY) Optional<String> authToken,
        @PathVariable int id
    ) {
        countService.count(id, 1);
        authService.validateAccess(id, authToken);
        var filePath = contentService.getPathById(id);
        return "forward:" + ARTICLE_FILE_ROOT_PATH + filePath;
    }

    @PostMapping("/api/login")
    public String requireToken(
        @RequestBody String password,
        @RequestParam(required = false) Optional<Integer> articleId,
        HttpServletResponse response
    ) {
        authService.authenticate(password, response);
        if (articleId.isEmpty()) {
            return "redirect:/";
        } else {
            return "redirect:/api/article/" + articleId.orElseThrow();
        }
    }
}
