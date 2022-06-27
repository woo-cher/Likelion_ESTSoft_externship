package com.shop.projectlion.global.interceptor;

import com.shop.projectlion.domain.jwt.constant.TokenType;
import com.shop.projectlion.domain.jwt.service.TokenManager;
import com.shop.projectlion.domain.member.exception.NotValidTokenException;
import com.shop.projectlion.global.error.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiAuthenticationInterceptor implements HandlerInterceptor {

    private final TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorizationValue = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Is exist `Authorization` value in header
        if (!StringUtils.hasText(authorizationValue)) {
            throw new NotValidTokenException(ErrorCode.NOT_EXISTS_AUTHORIZATION);
        }

        // Is Contain String "Bearer "
        if (isInvalidBearerHeader(authorizationValue)) {
            throw new NotValidTokenException(ErrorCode.NOT_VALID_BEARER_GRANT_TYPE);
        }

        // Is valid token
        String token = tokenManager.getBearerTokenInAuthHeader(authorizationValue);
        if (!tokenManager.validateToken(token)) {
            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
        }

        Claims claims = tokenManager.getTokenClaims(token);

        // Is token type `Access`
        if (!claims.getSubject().equals(TokenType.ACCESS.name())) {
            throw new NotValidTokenException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        // Is expired token
        if (tokenManager.isTokenExpired(claims.getExpiration())) {
            throw new NotValidTokenException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        }

        return true;
    }

    private boolean isInvalidBearerHeader(String actual) {
        Pattern pattern = Pattern.compile("^(Bearer ).*");
        Matcher matcher = pattern.matcher(actual);

        return !matcher.matches();
    }
}
