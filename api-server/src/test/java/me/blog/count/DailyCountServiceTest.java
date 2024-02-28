package me.blog.count;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DailyCountServiceTest {

    private final DailyCountRepository dailyCountRepository;
    private final DailyCountService dailyCountService;

    public DailyCountServiceTest(@Autowired DailyCountRepository dailyCountRepository) {
        this.dailyCountRepository = dailyCountRepository;
        this.dailyCountService = new DailyCountService(dailyCountRepository);
    }

    @DisplayName("날짜별 조회 횟수를 조회한다.")
    @Test
    public void findByDate() {
        var articleId = 1;
        var count = 20;
        var date = LocalDate.now();
        var saved = new DailyCount(articleId, count, date);
        dailyCountRepository.save(saved);

        var read = dailyCountService.findByDate(articleId, date).orElseThrow();
        assertEquals(saved, read);
        assertAll(
            () -> assertEquals(saved.getArticleId(), read.getArticleId()),
            () -> assertEquals(saved.getCount(), read.getCount()),
            () -> assertEquals(saved.getDate(), read.getDate())
        );
    }

    @DisplayName("아티클 별 조회 횟수를 기록한다.")
    @Test
    public void persist() {
        var articleId = 1;
        var addCount = 20;
        var date = LocalDate.now();
        var beforeCount = dailyCountService.findByDate(articleId, date)
            .map(DailyCount::getCount)
            .orElse(0);
        dailyCountService.persist(articleId, addCount, date);

        var afterCount = dailyCountService.findByDate(articleId, date).orElseThrow().getCount();
        assertEquals(addCount + beforeCount, afterCount);
    }
}