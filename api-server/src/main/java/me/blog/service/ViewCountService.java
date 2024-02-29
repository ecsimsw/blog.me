package me.blog.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.blog.config.Cached.CacheName;
import me.blog.domain.DailyCount;
import me.blog.domain.DailyCountRepository;
import me.blog.domain.DailyCount_;
import me.blog.domain.TotalCount;
import me.blog.domain.TotalCountRepository;
import me.blog.domain.TotalCount_;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ViewCountService {

    private final DailyCountRepository dailyCountRepository;
    private final TotalCountRepository totalCountRepository;

    @Cacheable(value = CacheName.DAILY_VIEW_COUNT, key = "#date")
    @Transactional(readOnly = true)
    public int viewCountAt(LocalDate date) {
        return dailyCountRepository.findAllByDate(date).stream()
            .mapToInt(DailyCount::getCount)
            .sum();
    }

    @Cacheable(value = CacheName.TOTAL_VIEW_COUNT)
    @Transactional(readOnly = true)
    public int viewCount() {
        return totalCountRepository.sum().orElse(0);
    }

    @Cacheable(value = CacheName.DAILY_TOP_VIEWED_ARTICLE, key = "{#n, #date}")
    @Transactional(readOnly = true)
    public List<DailyCount> findTopNArticle(int n, LocalDate date) {
        return dailyCountRepository.findAllByDate(
            date,
            PageRequest.of(0, n, Direction.DESC, DailyCount_.COUNT, DailyCount_.ARTICLE_ID)
        );
    }

    @Cacheable(value = CacheName.DAILY_TOP_VIEWED_ARTICLE, key = "#n")
    @Transactional(readOnly = true)
    public List<TotalCount> findTopNTotalCount(int n) {
        return totalCountRepository.findAll(
            PageRequest.of(0, n, Direction.DESC, TotalCount_.COUNT, TotalCount_.ARTICLE_ID)
        ).toList();
    }

    @Transactional
    public void count(int articleId, int count, LocalDate date) {
        var dailyCount = dailyCountRepository.findByArticleIdAndDate(articleId, date)
            .orElse(new DailyCount(articleId, 0, date));
        dailyCount.addCount(count);
        dailyCountRepository.save(dailyCount);

        var totalCount = totalCountRepository.findById(articleId)
            .orElse(new TotalCount(articleId, 0));
        totalCount.addCount(count);
        totalCountRepository.save(totalCount);
    }

    @Transactional(readOnly = true)
    public Optional<DailyCount> findDailyCount(int articleId, LocalDate date) {
        return dailyCountRepository.findByArticleIdAndDate(articleId, date);
    }

    @Transactional(readOnly = true)
    public int getTotalViewCountOf(int articleId) {
        return totalCountRepository.findById(articleId)
            .map(TotalCount::getCount)
            .orElse(0);
    }
}
