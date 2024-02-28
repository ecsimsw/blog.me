package me.blog.dto;

import me.blog.domain.Article;
import me.blog.domain.DailyCount;

public record DailyCountResponse(
    int articleId,
    String title,
    int count
) {
    public static DailyCountResponse of(DailyCount dailyCount, Article article) {
        return new DailyCountResponse(article.index(), article.title(), dailyCount.getCount());
    }
}
