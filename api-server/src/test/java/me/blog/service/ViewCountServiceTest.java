package me.blog.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import me.blog.domain.DailyCount;
import me.blog.domain.DailyCountRepository;
import me.blog.domain.TotalCountRepository;
import me.blog.service.ViewCountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ViewCountServiceTest {

    private final DailyCountRepository dailyCountRepository;
    private final TotalCountRepository totalCountRepository;
    private final ViewCountService viewCountService;

    public ViewCountServiceTest(
        @Autowired DailyCountRepository dailyCountRepository,
        @Autowired TotalCountRepository totalCountRepository
    ) {
        this.dailyCountRepository = dailyCountRepository;
        this.totalCountRepository = totalCountRepository;
        this.viewCountService = new ViewCountService(dailyCountRepository, totalCountRepository);
    }

    @DisplayName("날짜별 조회 횟수를 조회한다.")
    @Test
    public void findByDate() {
        var articleId = 1;
        var count = 20;
        var date = LocalDate.now();
        var saved = new DailyCount(articleId, count, date);
        dailyCountRepository.save(saved);

        var read = viewCountService.findDailyCountByDate(articleId, date).orElseThrow();
        assertEquals(saved, read);
        assertAll(
            () -> assertEquals(saved.getArticleId(), read.getArticleId()),
            () -> assertEquals(saved.getCount(), read.getCount()),
            () -> assertEquals(saved.getDate(), read.getDate())
        );
    }

    @DisplayName("날짜별 조회 횟수를 기록한다.")
    @Test
    public void persist() {
        var articleId = 1;
        var addCount = 20;
        var date = LocalDate.now();
        var beforeCount = viewCountService.findDailyCountByDate(articleId, date)
            .map(DailyCount::getCount)
            .orElse(0);
        viewCountService.persist(articleId, addCount, date);

        var afterCount = viewCountService.findDailyCountByDate(articleId, date).orElseThrow().getCount();
        assertEquals(addCount + beforeCount, afterCount);
    }

    @DisplayName("글의 전체 조회수를 확인한다.")
    @Test
    void getTotalViewCountOf() {
        var articleId = 1;
        var totalViewCountOf = viewCountService.getTotalViewCountOf(articleId);
        var expected = totalCountRepository.findById(articleId).map(it -> it.getCount()).orElse(0);
        assertEquals(expected, totalViewCountOf);
    }
}