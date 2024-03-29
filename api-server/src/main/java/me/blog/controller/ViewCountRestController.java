package me.blog.controller;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.blog.domain.Article;
import me.blog.service.ContentService;
import me.blog.service.ViewCountService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shutdown.core.ShutDown;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ShutDown(
    conditionOnActiveProfile = "failover",
    message = "service unavailable",
    status = HttpStatus.SERVICE_UNAVAILABLE,
    contentType = "application/json"
)
@RequiredArgsConstructor
@RestController
public class ViewCountRestController {

    private final ContentService contentService;
    private final ViewCountService viewCountService;

    @GetMapping("/api/view/daily")
    public ResponseEntity<Integer> viewCountDaily(@DateTimeFormat(iso = ISO.DATE) LocalDate date) {
        var viewCount = viewCountService.viewCountAt(date);
        return ResponseEntity.ok(viewCount);
    }

    @GetMapping("/api/view/total")
    public ResponseEntity<Integer> viewCountTotal() {
        var viewCount = viewCountService.viewCount();
        return ResponseEntity.ok(viewCount);
    }

    @GetMapping("/api/view/top/daily")
    public ResponseEntity<List<Article>> topNArticle(@DateTimeFormat(iso = ISO.DATE) LocalDate date, int top) {
        var topNDailyCount = viewCountService.findTopNArticle(top, date);
        var topNArticle = topNDailyCount.stream()
            .map(it -> contentService.findById(it.getArticleId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        return ResponseEntity.ok(topNArticle);
    }

    @GetMapping("/api/view/top/total")
    public ResponseEntity<List<Article>> topNArticle(Integer top) {
        var topNTotalCount = viewCountService.findTopNTotalCount(top);
        var topNArticle = topNTotalCount.stream()
            .map(it -> contentService.findById(it.getArticleId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        return ResponseEntity.ok(topNArticle);
    }
}
