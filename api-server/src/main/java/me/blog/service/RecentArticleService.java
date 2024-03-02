package me.blog.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import me.blog.config.MemoryCacheType.Cached;
import me.blog.crawler.TistoryIndexPage;
import me.blog.domain.RecentArticleRepository;
import me.blog.dto.RecentArticleResponse;
import me.blog.dto.TistoryArticleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecentArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecentArticleService.class);

    private final TistoryIndexPage indexPage;
    private final RecentArticleRepository recentArticleRepository;

    public RecentArticleService(
        @Value("${tistory.blog.endpoint}") String pageUrl,
        RecentArticleRepository recentArticleRepository
    ) {
        this.indexPage = new TistoryIndexPage(pageUrl);
        this.recentArticleRepository = recentArticleRepository;
    }

    @Transactional
    public void update() {
        if (hasRecentData()) {
            return;
        }
        LOGGER.info("crawl recent article");
        var articles = indexPage.articles().stream()
            .map(TistoryArticleResponse::toRecentArticle)
            .collect(Collectors.toList());
        recentArticleRepository.deleteAll();
        recentArticleRepository.saveAll(articles);
    }

    @Transactional
    public boolean hasRecentData() {
        var firstPage = recentArticleRepository.findAll(PageRequest.of(0, 1));
        if (!firstPage.hasContent()) {
            return false;
        }
        var firstIndex = firstPage.getContent().get(0);
        return ChronoUnit.HOURS.between(firstIndex.getUpdated(), LocalDateTime.now()) < 2;
    }

    @Cacheable(value = Cached.RECENT_ARTICLES, key = "#n")
    @Transactional
    public List<RecentArticleResponse> getAll(int n) {
        var articles = recentArticleRepository.findAll(PageRequest.of(0, n)).getContent();
        return RecentArticleResponse.listOf(articles);
    }
}
