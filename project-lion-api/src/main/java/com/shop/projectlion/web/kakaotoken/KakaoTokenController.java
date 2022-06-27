package com.shop.projectlion.web.kakaotoken;

import com.shop.projectlion.api.login.service.SocialLoginService;
import com.shop.projectlion.domain.member.constant.MemberType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class KakaoTokenController {

    private final SocialLoginService socialLoginService;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.redirect-url}")
    private String redirectUrl;

    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }

    @GetMapping("/auth/kakao/callback")
    public @ResponseBody String loginCallback(String code) {

        KakaoTokenRequestDto kakaoTokenRequestDto = KakaoTokenRequestDto.builder()
                .grant_type("authorization_code")
                .client_id(this.clientId)
                .redirect_url(this.redirectUrl)
                .code(code)
                .build();

        return "kakao token: " + socialLoginService.getSocialToken(MemberType.KAKAO, kakaoTokenRequestDto);
    }

}
