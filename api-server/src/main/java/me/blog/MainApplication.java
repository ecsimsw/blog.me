package me.blog;

import me.blog.crawler.TistoryArticle;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shutdown.EnableShutDown;

@EnableShutDown
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
//        SpringApplication app = new SpringApplication(MainApplication.class);
//        app.run(args);

        TistoryArticle
            .crawl("https://ecsimsw.tistory.com/entry/1000%EB%A7%8C%EA%B0%9C-%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%A1%9C-%EC%BF%BC%EB%A6%AC-%EC%84%B1%EB%8A%A5-%ED%99%95%EC%9D%B8%ED%95%98%EA%B8%B0")
            .download("./storage-articles/test3.html");
    }
}
