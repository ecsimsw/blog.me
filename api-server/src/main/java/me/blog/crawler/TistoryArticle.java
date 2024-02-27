package me.blog.crawler;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;

public record TistoryArticle(String url, String articleHtml) {

    private static final String ARTICLE_TITLE_ELEMENT_CLASS_NAME = "tit_post";
    private static final String ARTICLE_BODY_ELEMENT_CLASS_NAME = "tt_article_useless_p_margin";
    private static final String ARTICLE_FORMAT =
    """
        <meta charset="utf-8">
        <html lang="ko">
        <link rel="stylesheet" type="text/css" href="../style.css"/>
        <body>
        <div class="wrap-right">
            <div class="main ">
                <div class="area-main">
                     <div class="article-header">
                         %s            
                     </div>
                     <div class="article-view">
                         %s             
                     </div>
                </div>
            </main>
        </div>
        </body>
        </html>>
    """;

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

    public void download(String filePath) {
        try {
            File file = new File(filePath);
            Files.write(articleHtml.getBytes(), file);
            System.out.println("Download complete : " + filePath);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to write as file", e);
        }
    }
}
