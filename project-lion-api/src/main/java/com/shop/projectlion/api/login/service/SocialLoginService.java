package com.shop.projectlion.api.login.service;

import com.shop.projectlion.api.login.SocialLoginClient;
import com.shop.projectlion.api.login.SocialMemberInfo;
import com.shop.projectlion.api.login.factory.SocialApiClientFactory;
import com.shop.projectlion.api.login.factory.SocialApiEndPointFactory;
import com.shop.projectlion.domain.member.constant.MemberType;
import com.shop.projectlion.web.social.SocialTokenReqDto;
import com.shop.projectlion.web.social.SocialTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final SocialApiClientFactory apiClientFactory;
    private final SocialApiEndPointFactory apiEndPointFactory;

    public SocialTokenResponseDto getSocialToken(MemberType memberType, SocialTokenReqDto socialTokenRequestDto) {
        SocialLoginClient client = apiClientFactory.getSocialLoginClient(memberType);
        return client.getSocialTokenByAuth(socialTokenRequestDto);
    }

    public SocialMemberInfo getSocialMemberInfo(MemberType memberType, String socialAccessToken) throws URISyntaxException {
        SocialLoginClient socialLoginClient = apiClientFactory.getSocialLoginClient(memberType);

        URI apiHost = apiEndPointFactory.getMemberInfoApiEndPoint(memberType);
        return socialLoginClient.getSocialMemberInfoByToken(apiHost, socialAccessToken);
    }
}
