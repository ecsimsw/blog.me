package me.blog.count;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class DailyCountCacheService {

    private final DailyCountService dailyCountService;
    private final ConcurrentMap<Integer, Integer> countCachePerArticle;

    public DailyCountCacheService(DailyCountService dailyCountService) {
        this.dailyCountService = dailyCountService;
        this.countCachePerArticle = new ConcurrentHashMap<>();
    }

    public void count(int articleId, int count) {
        countCachePerArticle.compute(articleId, (k, v) -> {
            if(v == null) {
                return count;
            }
            return v + count;
        });
    }

    public int getCached(int articleId) {
        return countCachePerArticle.getOrDefault(articleId, 0);
    }

    @Scheduled(fixedDelay = 10_000)
    public void schedule() {
        for (var articleId : countCachePerArticle.keySet()) {
            dailyCountService.persist(articleId, countCachePerArticle.remove(articleId));
        }
    }
}
