package me.blog.count;

import java.time.LocalDate;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface DailyCountRepository extends JpaRepository<DailyCount, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<DailyCount> findByArticleIdAndDate(long articleId, LocalDate date);
}
