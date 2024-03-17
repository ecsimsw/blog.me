package me.blog.utils;

import org.checkerframework.checker.units.qual.A;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

public class TokenResolver implements HandlerMethodArgumentResolver {

    private final Class<A> resolveType;

    public TokenResolver(Class<A> resolveType) {
        this.resolveType = resolveType;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(resolveType);
    }

    @Override
    public Optional<String> resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        var request = (HttpServletRequest) webRequest.getNativeRequest();
        return Arrays.stream(request.getCookies())
            .filter(it -> it.getName().equals("ecsimsw-blog-token"))
            .map(Cookie::getValue)
            .findAny();
    }
}
