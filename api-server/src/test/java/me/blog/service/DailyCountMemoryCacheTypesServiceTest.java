package me.blog.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import me.blog.domain.DailyCount;
import me.blog.domain.DailyCountRepository;
import me.blog.domain.TotalCountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DailyCountMemoryCacheTypesServiceTest {

    private final DailyCountRepository dailyCountRepository;
    private final ViewCountCacheService viewCountCacheService;

    public DailyCountMemoryCacheTypesServiceTest(
        @Autowired DailyCountRepository dailyCountRepository,
        @Autowired TotalCountRepository totalCountRepository
    ) {
        this.dailyCountRepository = dailyCountRepository;
        this.viewCountCacheService = new ViewCountCacheService(
            new ViewCountService(dailyCountRepository, totalCountRepository)
        );
    }

    @DisplayName("날짜별 글 조회 수를 캐싱한다.")
    @Test
    public void count() {
        var articleId = 1;
        var addCount = 2;
        var before = viewCountCacheService.getCached(articleId);
        viewCountCacheService.count(articleId, addCount);
        var after = viewCountCacheService.getCached(articleId);
        assertEquals(before + addCount, after);
    }

    @DisplayName("캐시 데이터는 동시 요청에 안전하다.")
    @Test
    public void concurrentRequest() throws InterruptedException {
        var articleId = 1;
        var addCount = 2;
        var numberOfThreads = 1000;
        var service = Executors.newFixedThreadPool(numberOfThreads);
        var latch = new CountDownLatch(numberOfThreads);
        for (var i = 0; i < numberOfThreads; i++) {
            service.execute(() -> {
                try {
                    viewCountCacheService.count(articleId, addCount);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        assertEquals(numberOfThreads * addCount, viewCountCacheService.getCached(articleId));
    }

    @DisplayName("일정한 주기로 캐시 내용을 DB에 반영하고, 반영된 cache는 제거된다.")
    @Test
    public void schedule() {
        var articleId = 1;
        var addCount = 2;
        var date = LocalDate.now();
        var beforeCacheCount = viewCountCacheService.getCached(articleId);
        var beforeDbCount = dailyCountRepository.findByArticleIdAndDate(articleId, date)
            .orElse(new DailyCount(articleId, addCount, date))
            .getCount();

        viewCountCacheService.count(articleId, addCount);
        viewCountCacheService.schedule();

        var afterCacheCount = viewCountCacheService.getCached(articleId);
        var afterDbCount = dailyCountRepository.findByArticleIdAndDate(articleId, date)
            .orElse(new DailyCount(articleId, addCount, date))
            .getCount();

        assertAll(
            () -> assertEquals(beforeDbCount + beforeCacheCount, afterDbCount),
            () -> assertEquals(0, afterCacheCount)
        );
    }
}