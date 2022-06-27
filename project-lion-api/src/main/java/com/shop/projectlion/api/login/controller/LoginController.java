package com.shop.projectlion.api.login.controller;

import com.shop.projectlion.api.login.service.LoginService;
import com.shop.projectlion.domain.jwt.dto.TokenDto;
import com.shop.projectlion.domain.member.constant.MemberType;
import com.shop.projectlion.global.error.exception.BusinessException;
import com.shop.projectlion.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/oauth/login")
    public TokenDto login(@RequestBody Map<String, String> body, @RequestHeader(value = HttpHeaders.AUTHORIZATION)
            String authorizationHeader) throws URISyntaxException {

        String memberTypeValue = body.getOrDefault("memberType", null);
        MemberType memberType = MemberType.create(memberTypeValue);

        if (!ObjectUtils.nullSafeEquals(memberType, MemberType.KAKAO)) {
            throw new BusinessException(ErrorCode.INVALID_MEMBER_TYPE);
        }

        return loginService.doSocialLogin(memberType, authorizationHeader);
    }

    @PostMapping("/token")
    public TokenDto reissueToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return loginService.getAccessTokenByRefreshToken(authorizationHeader);
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        loginService.doLogout(authorizationHeader);
        return "logout success";
    }

}
