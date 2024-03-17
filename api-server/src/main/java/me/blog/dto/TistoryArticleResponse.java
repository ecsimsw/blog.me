package me.blog.dto;

import me.blog.domain.RecentArticle;

public record TistoryArticleResponse(
    String title,
    String link
) {
    public RecentArticle toRecentArticle() {
        return new RecentArticle(title, link);
    }
}
