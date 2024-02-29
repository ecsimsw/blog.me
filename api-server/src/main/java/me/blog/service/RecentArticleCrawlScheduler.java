package me.blog.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecentArticleCrawlScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyCountCacheService.class);

    private final RecentArticleService recentArticleService;

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.HOURS)
    public void getRecentArticle() {
        if (recentArticleService.hasRecentData()) {
            return;
        }
        LOGGER.info("update recent articles");
        recentArticleService.update();
    }
}
