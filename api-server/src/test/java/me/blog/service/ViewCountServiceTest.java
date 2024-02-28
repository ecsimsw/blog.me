package me.blog.service;

import static me.blog.fixture.Dummies.MOCK_DAILY_COUNT;
import static me.blog.fixture.Dummies.MOCK_TOTAL_COUNT;
import static me.blog.fixture.Dummies.TOP_N_OF_MOCK_DAILY_COUNT;
import static me.blog.fixture.Dummies.TOP_N_OF_MOCK_TOTAL_COUNT;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.stream.Collectors;
import me.blog.domain.DailyCount;
import me.blog.domain.DailyCountRepository;
import me.blog.domain.TotalCount;
import me.blog.domain.TotalCountRepository;
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

        var read = viewCountService.findDailyCount(articleId, date).orElseThrow();
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
        var beforeCount = viewCountService.findDailyCount(articleId, date)
            .map(DailyCount::getCount)
            .orElse(0);
        viewCountService.count(articleId, addCount, date);

        var afterCount = viewCountService.findDailyCount(articleId, date).orElseThrow().getCount();
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

    @DisplayName("해당 날짜의 모든 글 조회수 합을 반환한다.")
    @Test
    void sumAt() {
        var date = LocalDate.now();
        var addCount = 10;
        var numberOfArticle = 10;
        for (var i = 1; i <= numberOfArticle; i++) {
            dailyCountRepository.save(new DailyCount(i, addCount, date));
        }
        var sum = viewCountService.sumAt(date);
        assertEquals(numberOfArticle * addCount, sum);
    }

    @DisplayName("해당 날짜의 조회수가 가장 높은 N개의 DailyCount 를 반환한다.")
    @Test
    void findTopNDailyCount() {
        var date = LocalDate.now();
        var n = 2;
        var counts = MOCK_DAILY_COUNT(date);
        dailyCountRepository.saveAll(counts);
        var result = viewCountService.findTopNArticle(n, date).stream()
            .map(DailyCount::getArticleId)
            .collect(Collectors.toList());
        var expected = TOP_N_OF_MOCK_DAILY_COUNT(date, n).stream()
            .map(DailyCount::getArticleId)
            .collect(Collectors.toList());
        assertEquals(expected, result);
    }

    @DisplayName("전체 조회수가 가장 높은 N 개의 TotalCount 를 반환한다.")
    @Test
    void findTopNTotalCount() {
        var n = 2;
        totalCountRepository.saveAll(MOCK_TOTAL_COUNT);
        var result = viewCountService.findTopNTotalCount(n).stream()
            .map(TotalCount::getArticleId)
            .collect(Collectors.toList());
        var expected = TOP_N_OF_MOCK_TOTAL_COUNT(n).stream()
            .map(TotalCount::getArticleId)
            .collect(Collectors.toList());
        assertEquals(expected, result);
    }
}