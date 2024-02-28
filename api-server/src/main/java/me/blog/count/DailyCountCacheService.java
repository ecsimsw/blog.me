package me.blog.count;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class DailyCountCacheService {

    private final ViewCountService viewCountService;
    private final ConcurrentMap<Integer, Integer> countCachePerArticle;

    public DailyCountCacheService(ViewCountService viewCountService) {
        this.viewCountService = viewCountService;
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
            viewCountService.persist(articleId, countCachePerArticle.remove(articleId));
        }
    }
}
