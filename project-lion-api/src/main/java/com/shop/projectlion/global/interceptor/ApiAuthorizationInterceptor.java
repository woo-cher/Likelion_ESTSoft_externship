package com.shop.projectlion.global.interceptor;

import com.shop.projectlion.domain.jwt.service.TokenManager;
import com.shop.projectlion.domain.member.constant.Role;
import com.shop.projectlion.domain.member.exception.NotValidTokenException;
import com.shop.projectlion.global.error.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiAuthorizationInterceptor implements HandlerInterceptor {

    private final TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = tokenManager.getBearerTokenInAuthHeader(authorizationValue);

        Claims claims = tokenManager.getTokenClaims(token);

        if (!claims.get("role").equals(Role.ADMIN.name())) {
            throw new NotValidTokenException(ErrorCode.NOT_ADMIN_MEMBER);
        }

        return true;
    }

}
