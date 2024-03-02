package me.blog;

import me.blog.crawler.TistoryArticle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shutdown.EnableShutDown;

@EnableShutDown
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
//        var app = new SpringApplication(MainApplication.class);
//        app.run(args);

        TistoryArticle article = TistoryArticle.crawl(
            "https://ecsimsw.tistory.com/entry/%ED%8B%B0%EC%8A%A4%ED%86%A0%EB%A6%AC-%EB%B8%94%EB%A1%9C%EA%B7%B8-%EB%B0%B1%EC%97%85%EA%B3%BC-%EB%B8%94%EB%A1%9C%EA%B7%B8-%EC%84%9C%EB%B2%84-%EC%9A%B4%EC%98%81"
        );
        article.download("myblog.html");

        TistoryArticle article2 = TistoryArticle.crawl(
            "https://ecsimsw.tistory.com/entry/20200901-%EC%9D%BC%EA%B8%B0"
        );
        article2.download("giggle.html");


        TistoryArticle article3 = TistoryArticle.crawl(
            "https://ecsimsw.tistory.com/entry/2020-07-03-Geeks"
        );
        article3.download("geeks.html");
    }
}
