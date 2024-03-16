package me.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@ConditionalOnProperty(value = "schedule.crawl.recent.contents", havingValue = "true")
@RequiredArgsConstructor
@Service
public class RecentContentCrawlScheduler {

    private final RecentContentService recentContentService;

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.SECONDS)
    public void getRecentArticle() {
        recentContentService.update();
    }
}
