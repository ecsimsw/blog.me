package me.blog;

import me.blog.crawler.TistoryArticle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shutdown.EnableShutDown;

@EnableShutDown
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(MainApplication.class);
        app.run(args);
    }
}
