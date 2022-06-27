package com.shop.projectlion.api.login.kakao;

import com.shop.projectlion.api.login.SocialLoginClient;
import com.shop.projectlion.global.constant.SocialApiEndPoints;
import com.shop.projectlion.web.social.SocialTokenReqDto;
import com.shop.projectlion.web.social.SocialTokenResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;

@FeignClient(url = SocialApiEndPoints.KAUTH, name = "kakaoLoginFeignClient")
public interface KakaoLoginFeignClient extends SocialLoginClient {

    @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    SocialTokenResponseDto getSocialTokenByAuth(@SpringQueryMap SocialTokenReqDto socialTokenReqDto);

    @PostMapping(value = "/v2/user/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoUserInfo getSocialMemberInfoByToken(URI baseUrl, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String socialAccessToken);

}
