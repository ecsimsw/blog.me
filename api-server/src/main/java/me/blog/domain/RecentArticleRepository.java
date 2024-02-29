package me.blog.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentArticleRepository extends JpaRepository<RecentArticle, Integer> {
}
