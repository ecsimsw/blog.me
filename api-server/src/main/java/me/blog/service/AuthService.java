package me.blog.service;

import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import me.blog.domain.SafeBoxRepository;
import me.blog.dto.PasswordRequest;
import me.blog.exception.InvalidAccessException;
import me.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static me.blog.config.AuthTokenConfig.*;

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
        if (!safeBoxRepository.isExistsByArticleId(articleId)) {
            return;
        }
        try {
            var tokenValue = optToken.orElseThrow();
            JwtUtils.validate(tokenSecret, tokenValue);
        } catch (Exception e) {
            throw new InvalidAccessException("Auth needed");
        }
    }

    public void authenticate(PasswordRequest requestPassword, HttpServletResponse response) {
        if (!password.equals(requestPassword.password())) {
            throw new InvalidAccessException("Wrong password");
        }
        var token = JwtUtils.createToken(tokenSecret, JWT_PAYLOAD(), JWT_EXPIRED_TIME_MS);
        var cookie = TOKEN_COOKIE(token);
        response.addCookie(cookie);
    }
}
