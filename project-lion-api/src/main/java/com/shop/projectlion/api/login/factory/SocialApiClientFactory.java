package com.shop.projectlion.api.login.factory;

import com.shop.projectlion.api.login.kakao.KakaoLoginFeignClient;
import com.shop.projectlion.api.login.SocialLoginClient;
import com.shop.projectlion.domain.member.constant.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialApiClientFactory {

    private final KakaoLoginFeignClient kakaoLoginFeignClient;

    public SocialLoginClient getSocialLoginClient(MemberType memberType) {

        if (memberType.equals(MemberType.KAKAO)) {
            return kakaoLoginFeignClient;
        }

        return null;
    }

}
