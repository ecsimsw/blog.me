package me.blog.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.blog.dto.TistoryArticleResponse;
import org.jsoup.Jsoup;

public class TistoryIndexPage {

    private final String url;

    public TistoryIndexPage(String url) {
        this.url = url;
    }

    public List<TistoryArticleResponse> articles() {
        try {
            var conn = Jsoup.connect(url);
            var document = conn.get();

            var articleTitle = document.body().getElementsByClass("tit_post");
            var articleLink = document.body().getElementsByClass("link_post");

            var titles = articleTitle.stream()
                .skip(1)
                .map(it -> it.text())
                .collect(Collectors.toList());
            var links = articleLink.stream()
                .skip(1)
                .map(it -> it.attr("href"))
                .collect(Collectors.toList());

            var articles = new ArrayList();
            for (int i = 0; i < titles.size(); i++) {
                articles.add(new TistoryArticleResponse(
                    titles.get(i),
                    url + links.get(i))
                );
            }
            return articles;
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to crawl : " + url, e);
        }
    }
}
