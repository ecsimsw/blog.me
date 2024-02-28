package me.blog.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface DailyCountRepository extends JpaRepository<DailyCount, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<DailyCount> findByArticleIdAndDate(int articleId, LocalDate date);

    List<DailyCount> findAllByDate(LocalDate date);

    List<DailyCount> findAllByDate(LocalDate date, Pageable pageable);
}
