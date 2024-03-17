package me.blog.config;

import java.time.LocalDateTime;
import java.util.Map;

public class AuthConfig {

    public static final String TOKEN_COOKIE_KEY = "ecsimsw-blog-token";
    public static final int TOKEN_COOKIE_EXPIRED_MS = 60 * 60 * 24;
    public static final boolean TOKEN_COOKIE_HTTP_ONLY = true;

    public static final int JWT_EXPIRED_TIME_MS = 60 * 60 * 24;
    public static Map<String, Object> JWT_PAYLOAD() {
        return Map.of("createdTime", LocalDateTime.now().toString());
    }
}
