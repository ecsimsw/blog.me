package me.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import shutdown.EnableShutDown;

@EnableAsync
@EnableShutDown
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(MainApplication.class);
        app.run(args);
    }
}
