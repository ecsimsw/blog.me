package me.blog.domain;

import javax.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface RecentArticleRepository extends JpaRepository<RecentArticle, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Page<RecentArticle> findAll(Pageable pageable);
}
