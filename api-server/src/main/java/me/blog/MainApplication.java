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
            .crawl("https://swmobenz.tistory.com/36")
            .download("./storage-articles/test22.html");
    }
}
