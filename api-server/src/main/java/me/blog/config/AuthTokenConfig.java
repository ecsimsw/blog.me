package me.blog.config;


import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Map;

public class AuthTokenConfig {

    public static final String TOKEN_COOKIE_KEY = "ecsimsw-blog-token";
    public static final int TOKEN_COOKIE_EXPIRED_MS = 60 * 60 * 24;
    public static final boolean TOKEN_COOKIE_HTTP_ONLY = true;

    public static final int JWT_EXPIRED_TIME_MS = 60 * 60 * 24;
    public static Map<String, Object> JWT_PAYLOAD() {
        return Map.of("createdTime", LocalDateTime.now().toString());
    }
    public static Cookie TOKEN_COOKIE(String token) {
        var cookie = new Cookie(TOKEN_COOKIE_KEY, token);
        cookie.setMaxAge(TOKEN_COOKIE_EXPIRED_MS);
        cookie.setHttpOnly(TOKEN_COOKIE_HTTP_ONLY);
        return cookie;
    }
}
