package me.blog.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static final JacksonSerializer SERIALIZER = new JacksonSerializer(new ObjectMapper());

    public static String createToken(String secret, Map<String, Object> payloads, int expireTime) {
        var key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        var now = new Date();
        var expiration = new Date(now.getTime() + Duration.ofSeconds(expireTime).toMillis());
        return Jwts.builder()
            .serializeToJsonWith(SERIALIZER)
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setClaims(payloads)
            .setExpiration(expiration)
            .signWith(key)
            .compact();
    }

    public static void validate(String secret, String token) {
        var key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token);
    }

    public static <T> T getClaimValue(String secret, String token, String claimName, Class<T> requiredType) {
        var key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
            .deserializeJsonWith(new JacksonDeserializer(Maps.of(claimName, requiredType).build()))
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get(claimName, requiredType);
    }
}
