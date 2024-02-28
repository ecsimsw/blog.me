package me.blog.count;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ViewCountService {

    private final DailyCountRepository dailyCountRepository;
    private final TotalCountRepository totalCountRepository;

    @Transactional(readOnly = true)
    public Optional<DailyCount> findDailyCountByDate(int articleId, LocalDate date) {
        return dailyCountRepository.findByArticleIdAndDate(articleId, date);
    }

    @Transactional(readOnly = true)
    public int getTotalViewCountOf(int articleId) {
        return totalCountRepository.findById(articleId)
            .map(TotalCount::getCount)
            .orElse(0);
    }

    @Transactional
    public void persist(int articleId, int count) {
        persist(articleId, count, LocalDate.now());
    }

    @Transactional
    public void persist(int articleId, int count, LocalDate date) {
        var dailyCount = dailyCountRepository.findByArticleIdAndDate(articleId, date)
            .orElse(new DailyCount(articleId, 0, date));
        dailyCount.addCount(count);
        dailyCountRepository.save(dailyCount);

        var totalCount = totalCountRepository.findById(articleId)
            .orElse(new TotalCount(articleId, 0));
        totalCountRepository.save(totalCount);
    }
}
