package me.blog.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.blog.service.ViewCountService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MonitoringMetrics {

    private final ViewCountService viewCountService;

    @PostConstruct
    public void registerMetrics() {
        totalViewCount();
        yesterdayViewCount();
        todayViewCount();
    }

    private void totalViewCount() {
        Gauge.builder("viewCount_total", viewCountService, ViewCountService::viewCount)
            .register(Metrics.globalRegistry);
    }

    private void yesterdayViewCount() {
        Gauge.builder("viewCount_yesterday", viewCountService, it -> it.viewCountAt(LocalDate.now().minusDays(1)))
            .register(Metrics.globalRegistry);
    }

    private void todayViewCount() {
        Gauge.builder("viewCount_today", viewCountService, it -> it.viewCountAt(LocalDate.now()))
            .register(Metrics.globalRegistry);
    }
}
