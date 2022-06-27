package com.shop.projectlion.domain.member.validator;

import com.shop.projectlion.domain.member.repository.MemberRepository;
import com.shop.projectlion.global.error.exception.BusinessException;
import com.shop.projectlion.global.error.exception.ErrorCode;
import com.shop.projectlion.web.login.dto.MemberRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateBeforeSave(MemberRegisterDto dto) {
        if (isExistMember(dto)) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED_MEMBER);
        } else if (isMisMatchPassword(dto)) {
            throw new BusinessException(ErrorCode.MISMATCHED_PASSWORD);
        }
    }

    private boolean isExistMember(MemberRegisterDto dto) {
        return memberRepository.findByEmail(dto.getEmail()).isPresent();
    }

    private boolean isMisMatchPassword(MemberRegisterDto dto) {
        return !StringUtils.equals(dto.getPassword(), dto.getPassword2());
    }
}
