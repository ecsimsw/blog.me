package me.blog.config;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public enum MemoryCacheType {

    DAILY_VIEW_COUNT(Cached.DAILY_VIEW_COUNT, 2, TimeUnit.HOURS, 3),
    TOTAL_VIEW_COUNT(Cached.TOTAL_VIEW_COUNT, 10, TimeUnit.SECONDS, 1),
    DAILY_TOP_VIEWED_ARTICLE(Cached.DAILY_TOP_VIEWED_ARTICLE, 2, TimeUnit.HOURS, 3),
    TOTAL_TOP_VIEWED_ARTICLE(Cached.TOTAL_TOP_VIEWED_ARTICLE, 2, TimeUnit.HOURS, 1),
    RECENT_ARTICLES(Cached.RECENT_ARTICLES, 2, TimeUnit.HOURS, 2),
    RECENT_COMMENTS(Cached.RECENT_COMMENTS, 2, TimeUnit.HOURS, 2);

    public static class Cached {
        public static final String DAILY_VIEW_COUNT = "dailyViewCount";
        public static final String TOTAL_VIEW_COUNT = "totalViewCount";
        public static final String DAILY_TOP_VIEWED_ARTICLE = "dailyTopViewedArticle";
        public static final String TOTAL_TOP_VIEWED_ARTICLE = "totalTopViewedArticle";
        public static final String RECENT_ARTICLES = "recentArticles";
        public static final String RECENT_COMMENTS = "recentComments";
    }

    private final String name;
    private final long duration;
    private final TimeUnit timeUnit;
    private final long maximumSize;

    MemoryCacheType(String name, long duration, TimeUnit timeUnit, long maximumSize) {
        this.name = name;
        this.duration = duration;
        this.timeUnit = timeUnit;
        this.maximumSize = maximumSize;
    }
}
