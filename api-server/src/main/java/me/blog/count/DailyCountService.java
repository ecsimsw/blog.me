package me.blog.count;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DailyCountService {

    private final DailyCountRepository dailyCountRepository;

    public DailyCountService(DailyCountRepository dailyCountRepository) {
        this.dailyCountRepository = dailyCountRepository;
    }

    @Transactional(readOnly = true)
    public Optional<DailyCount> findByDate(int articleId, LocalDate date) {
        return dailyCountRepository.findByArticleIdAndDate(articleId, date);
    }

    @Transactional
    public void persist(int articleId, int count) {
        persist(articleId, count, LocalDate.now());
    }

    @Transactional
    public void persist(int articleId, int count, LocalDate date) {
        var current = dailyCountRepository.findByArticleIdAndDate(articleId, date)
            .orElse(new DailyCount(articleId, 0, date));
        current.addCount(count);
        dailyCountRepository.save(current);
    }
}
