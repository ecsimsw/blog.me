package me.blog.crawler;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;

public class TistoryArticle {

    private static final String ARTICLE_TITLE_ELEMENT_CLASS_NAME = "tit_post";
    private static final String ARTICLE_BODY_ELEMENT_CLASS_NAME = "tt_article_useless_p_margin";
    private static final String ARTICLE_FORMAT =
        "<meta charset=\"utf-8\">\n"
            + "<html lang=\"ko\">\n"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"../style.css\"/>\n"
            + "<body>\n"
            + "<div class=\"wrap-right\">\n"
            + "    <main class=\"main \">\n"
            + "        <div class=\"area-main\">\n"
            + "             <div class=\"article-header\">\n"
            + "                 %s"
            + "             </div>\n"
            + "             <div class=\"article-view\">\n"
            + "                 %s"
            + "             </div>\n"
            + "        </div>\n"
            + "    </main>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>"
            + ">";

    public static TistoryArticle crawl(String url) {
        return crawl(url, ARTICLE_TITLE_ELEMENT_CLASS_NAME, ARTICLE_BODY_ELEMENT_CLASS_NAME);
    }

    public static TistoryArticle crawl(String url, String titleClassName, String articleBodyClassName) {
        var conn = Jsoup.connect(url);
        try {
            var document = conn.get();
            var articleTitle = document.body().getElementsByClass(titleClassName);
            var articleBody = document.body().getElementsByClass(articleBodyClassName);
            var articleHtml = String.format(ARTICLE_FORMAT, articleTitle.html(), articleBody.html());
            return new TistoryArticle(url, articleHtml);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to crawl : " + url, e);
        }
    }

    private final String url;
    private final String articleHtml;

    public TistoryArticle(String url, String articleHtml) {
        this.url = url;
        this.articleHtml = articleHtml;
    }

    public void download(String filePath) {
        try {
            File file = new File(filePath);
            Files.write(articleHtml.getBytes(), file);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to write as file", e);
        }
    }

    public String url() {
        return url;
    }

    public String articleHtml() {
        return articleHtml;
    }
}
