package me.blog.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TotalCountRepository extends JpaRepository<TotalCount, Integer> {

    @Query("SELECT SUM(t.count) FROM TotalCount t")
    Optional<Integer> sum();
}
