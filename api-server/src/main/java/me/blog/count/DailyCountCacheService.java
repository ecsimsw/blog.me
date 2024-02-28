package me.blog.count;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class DailyCountCacheService {

    private final DailyCountService dailyCountService;
    private final ConcurrentMap<Integer, Integer> countCache;

    public DailyCountCacheService(DailyCountService dailyCountService) {
        this.dailyCountService = dailyCountService;
        this.countCache = new ConcurrentHashMap<>();
    }

    public void count(int articleId, int count) {
        countCache.compute(articleId, (k, v) -> {
            if(v == null) {
                return count;
            }
            return v + count;
        });
    }

    public int getCached(int articleId) {
        return countCache.getOrDefault(articleId, 0);
    }

    @Scheduled(fixedDelay = 10000)
    public void schedule() {
        for (var articleId : countCache.keySet()) {
            dailyCountService.persist(articleId, countCache.remove(articleId));
        }
    }
}
