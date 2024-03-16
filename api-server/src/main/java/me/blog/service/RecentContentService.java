package me.blog.service;

import static me.blog.config.MemoryCacheType.Cached.RECENT_ARTICLES;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import me.blog.config.MemoryCacheType;
import me.blog.config.MemoryCacheType.Cached;
import me.blog.crawler.TistoryIndexPage;
import me.blog.domain.RecentArticleRepository;
import me.blog.domain.RecentCommentRepository;
import me.blog.dto.RecentArticleResponse;
import me.blog.dto.RecentCommentResponse;
import me.blog.dto.TistoryArticleResponse;
import me.blog.dto.TistoryCommentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecentContentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecentContentService.class);
    private static final int TTL_RECENT_CONTENT_DATA_HOUR = 2;

    private final TistoryIndexPage indexPage;
    private final RecentArticleRepository recentArticleRepository;
    private final RecentCommentRepository recentCommentRepository;

    public RecentContentService(
        @Value("${tistory.blog.endpoint}") String pageUrl,
        RecentArticleRepository recentArticleRepository,
        RecentCommentRepository recentCommentRepository
    ) {
        this.indexPage = new TistoryIndexPage(pageUrl);
        this.recentArticleRepository = recentArticleRepository;
        this.recentCommentRepository = recentCommentRepository;
    }

    @Cacheable(value = RECENT_ARTICLES, key = "#n")
    @Transactional
    public List<RecentArticleResponse> recentArticles(int n) {
        LOGGER.info("crawl recent articles");
        var articles = indexPage.articles().stream()
            .map(TistoryArticleResponse::toRecentArticle)
            .toList();
        recentArticleRepository.deleteAll();
        recentArticleRepository.saveAll(articles);
        return RecentArticleResponse.listOf(articles.subList(0, n));
    }

    @Cacheable(value = Cached.RECENT_COMMENTS, key = "#n")
    @Transactional
    public List<RecentCommentResponse> recentComments(int n) {
        LOGGER.info("crawl recent comments");
        var comments = indexPage.comments().stream()
            .map(TistoryCommentResponse::toRecentComment)
            .toList();
        recentCommentRepository.deleteAll();
        recentCommentRepository.saveAll(comments);
        return RecentCommentResponse.listOf(comments.subList(0, n));
    }
}
