package com.shop.projectlion.api.login.factory;

import com.shop.projectlion.domain.member.constant.MemberType;
import com.shop.projectlion.global.constant.SocialApiEndPoints;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class SocialApiEndPointFactory {

    public URI getMemberInfoApiEndPoint(MemberType memberType) throws URISyntaxException {
        String url = memberType.equals(MemberType.KAKAO) ? SocialApiEndPoints.KAPI : "";

        // TODO: need check null and throw
        return new URI(url);
    }

}
