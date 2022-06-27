package com.shop.projectlion.web.kakaotoken;

import com.shop.projectlion.web.social.SocialTokenReqDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoTokenRequestDto extends SocialTokenReqDto {

    private String grant_type;
    private String client_id;
    private String redirect_url;
    private String code;

    @Builder
    public KakaoTokenRequestDto(String grant_type, String client_id, String redirect_url, String code) {
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.redirect_url = redirect_url;
        this.code = code;
    }

}
