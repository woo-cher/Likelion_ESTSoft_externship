package com.shop.projectlion.api.login.service;

import com.shop.projectlion.api.login.SocialMemberInfo;
import com.shop.projectlion.domain.jwt.constant.TokenType;
import com.shop.projectlion.domain.jwt.dto.TokenDto;
import com.shop.projectlion.domain.jwt.service.TokenManager;
import com.shop.projectlion.domain.member.constant.MemberType;
import com.shop.projectlion.domain.member.constant.Role;
import com.shop.projectlion.domain.member.entity.Member;
import com.shop.projectlion.domain.member.exception.NotValidTokenException;
import com.shop.projectlion.domain.member.exception.TokenNotFoundException;
import com.shop.projectlion.domain.member.repostiory.MemberRepository;
import com.shop.projectlion.global.error.exception.ErrorCode;
import com.shop.projectlion.global.util.DateTimeUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final TokenManager tokenManager;
    private final MemberRepository memberRepository;
    private final SocialLoginService socialLoginService;

    @Transactional
    public TokenDto doSocialLogin(MemberType memberType, String socialAccessToken) throws URISyntaxException {

        SocialMemberInfo socialMemberInfo = socialLoginService.getSocialMemberInfo(memberType, socialAccessToken);
        String email = socialMemberInfo.getSocialEmail();

        Optional<Member> dbMember = memberRepository.findByMemberTypeAndEmail(memberType, email);

        if (dbMember.isEmpty()) {
            Member socialMember = Member.createSocialMember(socialMemberInfo.getSocialName(), email, memberType);
            TokenDto tokenDto = tokenManager.createTokenDto(email, Role.ADMIN);

            socialMember.updateRefreshToken(tokenDto.getRefreshToken());
            socialMember.updateTokenExpirationTime(DateTimeUtils.convertToLocalDateTime(tokenDto.getRefreshTokenExpireTime()));
            memberRepository.save(socialMember);

            return tokenDto;
        } else {
            Member member = dbMember.get();
            TokenDto tokenDto = tokenManager.createTokenDto(email, member.getRole());

            member.updateRefreshToken(tokenDto.getRefreshToken());
            member.updateTokenExpirationTime(DateTimeUtils.convertToLocalDateTime(tokenDto.getRefreshTokenExpireTime()));

            return tokenDto;
        }
    }

    @Transactional
    public TokenDto getAccessTokenByRefreshToken(String authorizationHeader) {
        String refreshToken = tokenManager.getBearerTokenInAuthHeader(authorizationHeader);

        Claims claims = getTokenClaimAfterValidation(refreshToken, TokenType.REFRESH);

        Member dbMember = memberRepository.findByEmail(claims.getAudience())
                .orElseThrow(() -> new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN));

        if (!StringUtils.equals(dbMember.getRefreshToken(), refreshToken)) {
            throw new TokenNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        return tokenManager.createTokenDtoOnlyAccessToken(dbMember.getEmail(), dbMember.getRole());
    }

    @Transactional
    public void doLogout(String authorizationHeader) {
        String accessToken = tokenManager.getBearerTokenInAuthHeader(authorizationHeader);

        Claims claims = getTokenClaimAfterValidation(accessToken, TokenType.ACCESS);

        Member dbMember = memberRepository.findByEmail(claims.getAudience())
                .orElseThrow(() -> new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN));

        Date tokenExpiredTime = new Date(System.currentTimeMillis());

        String expiredRefreshToken = tokenManager.createRefreshToken(dbMember.getEmail(), tokenExpiredTime);
        LocalDateTime expiredTimeLdt = DateTimeUtils.convertToLocalDateTime(tokenExpiredTime);

        dbMember.updateRefreshToken(expiredRefreshToken);
        dbMember.updateTokenExpirationTime(expiredTimeLdt);
    }

    private Claims getTokenClaimAfterValidation(String jwtToken, TokenType validTokenType) {

        if (!tokenManager.validateToken(jwtToken)) {
            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
        }

        Claims claims = tokenManager.getTokenClaims(jwtToken);

        if (!StringUtils.equals(claims.getSubject(), validTokenType.name())) {
            ErrorCode errorCode = validTokenType.equals(TokenType.ACCESS) ?
                    ErrorCode.NOT_ACCESS_TOKEN_TYPE : ErrorCode.NOT_REFRESH_TOKEN_TYPE;

            throw new NotValidTokenException(errorCode);
        }

        if (tokenManager.isTokenExpired(claims.getExpiration())) {
            ErrorCode errorCode = validTokenType.equals(TokenType.ACCESS) ?
                    ErrorCode.ACCESS_TOKEN_EXPIRED : ErrorCode.REFRESH_TOKEN_EXPIRED;

            throw new NotValidTokenException(errorCode);
        }

        return claims;
    }

}
