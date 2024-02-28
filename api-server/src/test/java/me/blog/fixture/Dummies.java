package me.blog.fixture;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import me.blog.domain.Article;
import me.blog.domain.Category;
import me.blog.domain.DailyCount;
import me.blog.domain.TotalCount;

public class Dummies {

    public static final List<Article> MOCK_ARTICLE_DATA = List.of(
        new Article(1, "A", 1, "A.html"),
        new Article(2, "B", 1, "B.html"),
        new Article(3, "C", 1, "C.html"),
        new Article(4, "D", 1, "D.html"),
        new Article(5, "F", 2, "F.html"),
        new Article(6, "G", 2, "G.html")
    );

    public static final List<Category> MOCK_CATEGORY_DATA = List.of(
        new Category(1, "A"),
        new Category(2, "B"),
        new Category(3, "C"),
        new Category(4, "D"),
        new Category(5, "E")
    );

    public static List<DailyCount> MOCK_DAILY_COUNT(LocalDate date) {
        return List.of(
            new DailyCount(1, 1, date),
            new DailyCount(2, 2, date),
            new DailyCount(3, 3, date),
            new DailyCount(4, 4, date),
            new DailyCount(5, 4, date),
            new DailyCount(6, 4, date)
        );
    }

    public static List<DailyCount> TOP_N_OF_MOCK_DAILY_COUNT(LocalDate date, int n) {
        return MOCK_DAILY_COUNT(date).stream().sorted((o1, o2) -> {
                if (o2.getCount() == o1.getCount()) {
                    return o2.getArticleId() - o1.getArticleId();
                }
                return o2.getCount() - o1.getCount();
            })
            .limit(n)
            .collect(Collectors.toList());
    }

    public static List<TotalCount> MOCK_TOTAL_COUNT = List.of(
        new TotalCount(1, 1),
        new TotalCount(2, 2),
        new TotalCount(3, 3),
        new TotalCount(4, 4),
        new TotalCount(5, 4),
        new TotalCount(6, 4)
    );

    public static List<TotalCount> TOP_N_OF_MOCK_TOTAL_COUNT(int n) {
        return MOCK_TOTAL_COUNT.stream().sorted((o1, o2) -> {
                if (o2.getCount() == o1.getCount()) {
                    return o2.getArticleId() - o1.getArticleId();
                }
                return o2.getCount() - o1.getCount();
            })
            .limit(n)
            .collect(Collectors.toList());
    }

}
