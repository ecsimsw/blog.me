package me.blog.count;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TotalCountRepository extends JpaRepository<TotalCount, Integer> {
}
