package me.blog.controller;

import lombok.RequiredArgsConstructor;
import me.blog.dto.RecentArticleResponse;
import me.blog.dto.RecentCommentResponse;
import me.blog.service.RecentContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shutdown.core.ShutDown;

import java.util.List;

@ShutDown(
    conditionOnActiveProfile = "failover",
    message = "service unavailable",
    status = HttpStatus.SERVICE_UNAVAILABLE,
    contentType = "application/json"
)
@RequiredArgsConstructor
@RestController
public class RecentArticleRestController {

    private final RecentContentService recentContentService;

    @GetMapping("/api/recent/article")
    public ResponseEntity<List<RecentArticleResponse>> recentArticle(int n) {
        var result = recentContentService.recentArticles(n);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/recent/comment")
    public ResponseEntity<List<RecentCommentResponse>> recentComment(int n) {
        var result = recentContentService.recentComments(n);
        return ResponseEntity.ok(result);
    }
}
