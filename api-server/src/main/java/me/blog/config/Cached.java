package me.blog.config;

import java.util.concurrent.TimeUnit;
import lombok.Getter;

@Getter
public enum Cached {

    DAILY_VIEW_COUNT(VALUES.DAILY_VIEW_COUNT, 1, TimeUnit.HOURS, 3),
    TOTAL_VIEW_COUNT(VALUES.TOTAL_VIEW_COUNT, 1, TimeUnit.MINUTES, 1),
    DAILY_TOP_VIEWED_ARTICLE(VALUES.DAILY_TOP_VIEWED_ARTICLE, 10, TimeUnit.MINUTES, 3),
    TOTAL_TOP_VIEWED_ARTICLE(VALUES.TOTAL_TOP_VIEWED_ARTICLE, 1, TimeUnit.HOURS, 1),
    RECENT_ARTICLES(VALUES.RECENT_ARTICLES, 2, TimeUnit.HOURS, 1);

    public static class VALUES {
        public static final String DAILY_VIEW_COUNT = "dailyViewCount";
        public static final String TOTAL_VIEW_COUNT = "totalViewCount";
        public static final String DAILY_TOP_VIEWED_ARTICLE = "dailyTopViewedArticle";
        public static final String TOTAL_TOP_VIEWED_ARTICLE = "totalTopViewedArticle";
        public static final String RECENT_ARTICLES = "recentArticles";
    }

    private final String name;
    private final long duration;
    private final TimeUnit timeUnit;
    private final long maximumSize;

    Cached(String name, long duration, TimeUnit timeUnit, long maximumSize) {
        this.name = name;
        this.duration = duration;
        this.timeUnit = timeUnit;
        this.maximumSize = maximumSize;
    }
}