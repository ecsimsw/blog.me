package me.blog.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TotalCountRepository extends JpaRepository<TotalCount, Integer> {

    @Query("SELECT SUM(t.count) FROM TotalCount t")
    Optional<Integer> sum();
}
