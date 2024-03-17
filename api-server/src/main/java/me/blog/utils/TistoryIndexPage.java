package me.blog.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.blog.dto.TistoryCommentResponse;
import me.blog.dto.TistoryArticleResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TistoryIndexPage {

    private final String url;
    private final Document document;

    public TistoryIndexPage(String url) {
        try {
            this.url = url;
            this.document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to crawl : " + url, e);
        }
    }

    public List<TistoryCommentResponse> comments() {
        var comments = document.body().getElementsByClass("link_board");
        return comments.stream()
            .map(it -> new TistoryCommentResponse(it.text(), url + it.attr("href")))
            .toList();
    }

    public List<TistoryArticleResponse> articles() {
        var articleTitle = document.body().getElementsByClass("tit_post");
        var articleLink = document.body().getElementsByClass("link_post");

        var titles = articleTitle.stream()
            .skip(1)
            .map(it -> it.text())
            .toList();
        var links = articleLink.stream()
            .skip(1)
            .map(it -> it.attr("href"))
            .toList();

        var articles = new ArrayList();
        for (int i = 0; i < titles.size(); i++) {
            articles.add(new TistoryArticleResponse(
                titles.get(i),
                url + links.get(i))
            );
        }
        return articles;
    }
}
