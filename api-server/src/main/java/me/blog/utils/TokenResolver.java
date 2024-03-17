package me.blog.utils;

import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class TokenResolver<T extends AuthToken> implements HandlerMethodArgumentResolver {

    private final Class<T> resolveType;

    public TokenResolver(Class<T> resolveType) {
        this.resolveType = resolveType;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(resolveType);
    }

    @Override
    public Optional<String> resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        var authAnnotation = parameter.getParameterAnnotation(resolveType);
        if (authAnnotation == null) {
            return Optional.empty();
        }
        var cookies = ((HttpServletRequest) webRequest.getNativeRequest()).getCookies();
        if(cookies == null || cookies.length == 0) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
            .filter(it -> it.getName().equals(authAnnotation.tokenKey()))
            .map(Cookie::getValue)
            .findAny();
    }
}
