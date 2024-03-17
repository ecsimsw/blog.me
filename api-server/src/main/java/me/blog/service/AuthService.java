package me.blog.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import me.blog.domain.SafeBoxRepository;
import me.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final String password;
    private final String tokenSecret;
    private final SafeBoxRepository safeBoxRepository;

    public AuthService(
        @Value("${article.auth.password}") String password,
        @Value("${article.auth.token.secret}") String tokenSecret,
        SafeBoxRepository safeBoxRepository
    ) {
        this.password = password;
        this.tokenSecret = tokenSecret;
        this.safeBoxRepository = safeBoxRepository;
    }

    public void validateAccess(int articleId, Optional<String> optToken) {
        if(!safeBoxRepository.isExistsByArticleId(articleId)) {
            return;
        }
        try {
            var tokenValue = optToken.orElseThrow();
            JwtUtils.validate(tokenSecret, tokenValue);
        } catch (Exception e) {
            throw new IllegalArgumentException("Auth needed");
        }
    }

    public void authenticate(String requestPassword, HttpServletResponse response) {
        if(!this.password.equals(requestPassword)) {
            throw new IllegalArgumentException("Wrong password" + requestPassword + " " + password);
        }
        var token = JwtUtils.createToken(tokenSecret, Map.of("createdTime", LocalDateTime.now().toString()), 3000);
        Cookie cookie = new Cookie("ecsimsw-blog-token", token);
        cookie.setMaxAge(3000);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
