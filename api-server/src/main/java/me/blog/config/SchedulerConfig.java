package me.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@EnableScheduling
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    private static final int NUMBER_OF_THREAD = 5;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        var threadPool = new ThreadPoolTaskScheduler();
        threadPool.setPoolSize(NUMBER_OF_THREAD);
        threadPool.initialize();
        taskRegistrar.setTaskScheduler(threadPool);
    }
}
