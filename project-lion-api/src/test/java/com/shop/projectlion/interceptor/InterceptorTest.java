package com.shop.projectlion.interceptor;

import com.shop.projectlion.domain.jwt.constant.TokenType;
import com.shop.projectlion.domain.jwt.service.TokenManager;
import com.shop.projectlion.domain.member.constant.Role;
import com.shop.projectlion.domain.member.exception.NotValidTokenException;
import com.shop.projectlion.global.error.exception.ErrorCode;
import com.shop.projectlion.global.interceptor.ApiAuthenticationInterceptor;
import com.shop.projectlion.global.interceptor.ApiAuthorizationInterceptor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("인터셉터 예외 테스트")
public class InterceptorTest {

    @InjectMocks
    ApiAuthenticationInterceptor authenticationInterceptor;

    @InjectMocks
    ApiAuthorizationInterceptor authorizationInterceptor;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    HttpServletResponse httpServletResponse;

    @Mock
    TokenManager tokenManager;

    String mockAccessToken;
    String mockRefreshToken;

    @BeforeEach
    void setup() {
        this.mockAccessToken = getMockToken(TokenType.ACCESS, Role.ADMIN);
        this.mockRefreshToken = getMockToken(TokenType.REFRESH, null);
    }

    @Nested
    @DisplayName("사용자 인증 인터셉터 테스트")
    class AuthenticationInterceptor {

        @Test
        @DisplayName("Header Authorization 값이 비었을 때")
        void EmptyAuthorizationValue() {
            // empty `Authorization` value in header
            given(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn(null);

            Throwable throwable = catchThrowable(() ->
                    authenticationInterceptor.preHandle(httpServletRequest, httpServletResponse, any(Object.class)));

            assertEquals(throwable.getMessage(), ErrorCode.NOT_EXISTS_AUTHORIZATION.getMessage());
            assertTrue(throwable instanceof NotValidTokenException);
        }

        @Test
        @DisplayName("Authorization 값에 `Bearer ` 패턴이 없을 때")
        void EmptyBearerString() {
            // empty `Bearer ` in header
            given(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn(mockAccessToken);

            Throwable throwable = catchThrowable(() ->
                    authenticationInterceptor.preHandle(httpServletRequest, httpServletResponse, any(Object.class)));

            assertEquals(throwable.getMessage(), ErrorCode.NOT_VALID_BEARER_GRANT_TYPE.getMessage());
            assertTrue(throwable instanceof NotValidTokenException);

            // empty space like `Bearerabce`
            given(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn("Bearer" + mockAccessToken);

            throwable = catchThrowable(() ->
                    authenticationInterceptor.preHandle(httpServletRequest, httpServletResponse, any(Object.class)));

            assertEquals(throwable.getMessage(), ErrorCode.NOT_VALID_BEARER_GRANT_TYPE.getMessage());
            assertTrue(throwable instanceof NotValidTokenException);
        }

        @Test
        @DisplayName("유효하지 않은 토큰으로 요청할 때")
        void InvalidToken() {
            // invalid token
            given(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn("Bearer imNotToken!");
            given(tokenManager.getBearerTokenInAuthHeader(anyString())).willReturn("imNotToken!");
            given(tokenManager.validateToken(anyString())).willReturn(false);

            Throwable throwable = catchThrowable(() ->
                    authenticationInterceptor.preHandle(httpServletRequest, httpServletResponse, any(Object.class)));

            assertEquals(throwable.getMessage(), ErrorCode.NOT_VALID_TOKEN.getMessage());
            assertTrue(throwable instanceof NotValidTokenException);
        }

        @Test
        @DisplayName("엑세스 토큰이 아닌 토큰으로 요청할 때")
        void isNotAccessToken() {
            // is not access token
            given(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn("Bearer " + mockRefreshToken);
            given(tokenManager.getBearerTokenInAuthHeader(anyString())).willReturn("Bearer " + mockRefreshToken);
            given(tokenManager.getTokenClaims(anyString())).willReturn(getMockTokenClaims(mockRefreshToken));
            given(tokenManager.validateToken(anyString())).willReturn(true);

            Throwable throwable = catchThrowable(() ->
                    authenticationInterceptor.preHandle(httpServletRequest, httpServletResponse, any(Object.class)));

            assertEquals(throwable.getMessage(), ErrorCode.NOT_ACCESS_TOKEN_TYPE.getMessage());
            assertTrue(throwable instanceof NotValidTokenException);
        }

        @Test
        @DisplayName("만료된 엑세스 토큰을 요청할 때")
        void expiredToken() {
            given(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn("Bearer " + mockAccessToken);
            given(tokenManager.getBearerTokenInAuthHeader(anyString())).willReturn(mockAccessToken);
            given(tokenManager.getTokenClaims(anyString())).willReturn(getMockTokenClaims(mockAccessToken));
            given(tokenManager.validateToken(anyString())).willReturn(true);
            given(tokenManager.isTokenExpired(any())).willReturn(true);

            Throwable throwable = catchThrowable(() ->
                    authenticationInterceptor.preHandle(httpServletRequest, httpServletResponse, any(Object.class)));

            assertEquals(throwable.getMessage(), ErrorCode.ACCESS_TOKEN_EXPIRED.getMessage());
            assertTrue(throwable instanceof NotValidTokenException);
        }

    }

    @Nested
    @DisplayName("사용자 인가 인터셉터 테스트")
    class AuthorizationInterceptor {

        @Test
        @DisplayName("일반 사용자가 API 요청을 했을 때")
        void notAdmin() {
            String notAdminToken = getMockToken(TokenType.ACCESS, Role.USER);

            given(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn("Bearer " + notAdminToken);
            given(tokenManager.getBearerTokenInAuthHeader(anyString())).willReturn(notAdminToken);
            given(tokenManager.getTokenClaims(anyString())).willReturn(getMockTokenClaims(notAdminToken));

            Throwable throwable = catchThrowable(() ->
                    authorizationInterceptor.preHandle(httpServletRequest, httpServletResponse, any(Object.class)));

            assertEquals(throwable.getMessage(), ErrorCode.NOT_ADMIN_MEMBER.getMessage());
            assertTrue(throwable instanceof NotValidTokenException);
        }
    }

    private String getMockToken(TokenType tokenType, Role role) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(tokenType.name())
                .setAudience("email")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secret")
                .setHeaderParam("typ", "JWT");

        if (tokenType.equals(TokenType.ACCESS)) {
            return builder.claim("role", role).compact();
        }

        return builder.compact();
    }

    private Claims getMockTokenClaims(String mockToken) {
        return Jwts.parser().setSigningKey("secret")
                .parseClaimsJws(mockToken).getBody();
    }

}
