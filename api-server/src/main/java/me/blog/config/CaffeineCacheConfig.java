package me.blog.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CaffeineCacheConfig implements CachingConfigurer {

    @Bean
    public CacheManager caffeineConfig() {
        var caches = Arrays.stream(Cached.values())
            .map(this::build)
            .collect(Collectors.toList());
        var cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    private CaffeineCache build(Cached cached) {
        return new CaffeineCache(cached.getName(), Caffeine.newBuilder()
            .expireAfterWrite(cached.getDuration(), cached.getTimeUnit())
            .maximumSize(cached.getMaximumSize())
            .build()
        );
    }
}
