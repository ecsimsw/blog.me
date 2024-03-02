package me.blog.dto;

import me.blog.domain.RecentArticle;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record RecentArticleResponse(
    int no,
    String title,
    String url
) {

    public static List<RecentArticleResponse> listOf(List<RecentArticle> recentArticles) {
        return IntStream.range(0, recentArticles.size())
            .mapToObj(i -> new RecentArticleResponse(
                i + 1,
                recentArticles.get(i).getTitle(),
                recentArticles.get(i).getLink()
            )).collect(Collectors.toList());
    }
}
