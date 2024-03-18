package me.blog.controller;

import static me.blog.config.FilePathVariables.ARTICLE_FILE_ROOT_PATH;
import static me.blog.config.AuthTokenConfig.TOKEN_COOKIE_KEY;
import static me.blog.config.FilePathVariables.LOGIN_FILE_PATH;

import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.blog.dto.PasswordRequest;
import me.blog.exception.InvalidAccessException;
import me.blog.service.AuthService;
import me.blog.service.ContentService;
import me.blog.service.DailyCountCacheService;
import me.blog.utils.AuthToken;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class ArticleFileController {

    private final ContentService contentService;
    private final DailyCountCacheService countService;
    private final AuthService authService;

    @GetMapping("/api/article/{id}")
    public String serveArticleFile(
        @AuthToken(tokenKey = TOKEN_COOKIE_KEY) Optional<String> authToken,
        @PathVariable int id,
        RedirectAttributes redirectAttributes
    ) {
        countService.count(id, 1);
        try {
            authService.validateAccess(id, authToken);
            var filePath = contentService.getPathById(id);
            return "forward:" + ARTICLE_FILE_ROOT_PATH + filePath;
        } catch (InvalidAccessException e) {
            redirectAttributes.addAttribute("articleId", id);
            return "redirect:" + LOGIN_FILE_PATH;
        }
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> requireToken(@RequestBody PasswordRequest password, HttpServletResponse response) {
        authService.authenticate(password, response);
        return ResponseEntity.ok().build();
    }
}
