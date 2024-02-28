package me.blog.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.blog.domain.Article;
import me.blog.service.ContentService;
import me.blog.service.ViewCountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ViewCountRestController {

    private final ContentService contentService;
    private final ViewCountService viewCountService;

    @GetMapping("/api/view")
    public ResponseEntity<Integer> viewCount(LocalDate date) {
        var viewCount = viewCountService.sumAt(date);
        return ResponseEntity.ok(viewCount);
    }

    @GetMapping("/api/view/top/daily")
    public ResponseEntity<List<Article>> topNArticle(LocalDate date, int top) {
        var topNDailyCount = viewCountService.findTopNArticle(top, date);
        var topNArticle = topNDailyCount.stream()
            .map(it -> contentService.findById(it.getArticleId()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(topNArticle);
    }

    @GetMapping("/api/view/top/total")
    public ResponseEntity<List<Article>> topNArticle(int top) {
        var topNTotalCount = viewCountService.findTopNTotalCount(top);
        var topNArticle = topNTotalCount.stream()
            .map(it -> contentService.findById(it.getArticleId()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(topNArticle);
    }
}
