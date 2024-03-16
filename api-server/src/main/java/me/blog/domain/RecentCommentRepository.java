package me.blog.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentCommentRepository extends JpaRepository<RecentComment, Long> {
}
