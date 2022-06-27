package com.shop.projectlion.domain.member.constant;

import org.thymeleaf.util.StringUtils;

public enum MemberType {

    GENERAL,
    KAKAO,
    NAVER,
    GOOGLE;

    public static MemberType create(String value) {
        for (MemberType memberType : MemberType.values()) {
            if (StringUtils.equals(memberType.name(), value)) {
                return memberType;
            }
        }

        return null;
    }
}
