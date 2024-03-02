package me.blog.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class DailyCountCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyCountCacheService.class);

    private final ViewCountService viewCountService;
    private final ConcurrentMap<Integer, Integer> countCachePerArticle = new ConcurrentHashMap<>();

    public void count(int articleId, int count) {
        countCachePerArticle.compute(articleId, (k, v) -> {
            if (v == null) {
                return count;
            }
            return v + count;
        });
    }

    public int getCached(int articleId) {
        return countCachePerArticle.getOrDefault(articleId, 0);
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void schedule() {
        LOGGER.info("view count cache flush");
        for (var articleId : countCachePerArticle.keySet()) {
            viewCountService.count(articleId, countCachePerArticle.remove(articleId), LocalDate.now());
        }
    }
}
