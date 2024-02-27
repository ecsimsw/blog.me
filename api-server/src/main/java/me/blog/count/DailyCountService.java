package me.blog.count;

import java.time.LocalDate;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DailyCountService {

    private final DailyCountRepository dailyCountRepository;

    public DailyCountService(DailyCountRepository dailyCountRepository) {
        this.dailyCountRepository = dailyCountRepository;
    }

    @Transactional
    public void persist(long articleId, int count) {
        var date = LocalDate.now();
        var current = dailyCountRepository.findByArticleIdAndDate(articleId, date)
            .orElse(new DailyCount(articleId, date, 0));
        current.addCount(count);
        dailyCountRepository.save(current);
    }
}
