package me.blog.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.blog.dto.RecentArticleResponse;
import me.blog.service.RecentArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RecentArticleRestController {

    private final RecentArticleService recentArticleService;

    @GetMapping("/api/article/recent")
    public ResponseEntity<List<RecentArticleResponse>> recentArticle(int n) {
        var result = recentArticleService.getAll(n);
        return ResponseEntity.ok(result);
    }
}
