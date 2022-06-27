package com.shop.projectlion.api.login;

import com.shop.projectlion.web.social.SocialTokenReqDto;
import com.shop.projectlion.web.social.SocialTokenResponseDto;

import java.net.URI;

public interface SocialLoginClient {

    SocialTokenResponseDto getSocialTokenByAuth(SocialTokenReqDto socialTokenReqDto);
    SocialMemberInfo getSocialMemberInfoByToken(URI baseUrl, String socialAccessToken);

}
