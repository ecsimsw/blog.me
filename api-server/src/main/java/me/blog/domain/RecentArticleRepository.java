package me.blog.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;

public interface RecentArticleRepository extends JpaRepository<RecentArticle, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Page<RecentArticle> findAll(Pageable pageable);
}
