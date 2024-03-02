package me.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@ConditionalOnProperty(value = "schedule.crawl.recent.posts", havingValue = "true")
@RequiredArgsConstructor
@Service
public class RecentArticleCrawlScheduler {

    private final RecentArticleService recentArticleService;

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.HOURS)
    public void getRecentArticle() {
        recentArticleService.update();
    }
}
