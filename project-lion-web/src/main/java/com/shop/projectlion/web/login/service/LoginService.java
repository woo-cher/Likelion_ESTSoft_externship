package com.shop.projectlion.web.login.service;

import com.shop.projectlion.domain.member.entity.Member;
import com.shop.projectlion.domain.member.repository.MemberRepository;
import com.shop.projectlion.domain.member.type.MemberRole;
import com.shop.projectlion.domain.member.type.MemberType;
import com.shop.projectlion.domain.member.validator.MemberValidator;
import com.shop.projectlion.web.login.dto.MemberRegisterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    public void save(MemberRegisterDto dto) {
        String newPassword = new BCryptPasswordEncoder().encode(dto.getPassword());

        memberValidator.validateBeforeSave(dto);

        Member member = Member.builder()
                .memberName(dto.getName())
                .email(dto.getEmail())
                .password(newPassword)
                .memberType(MemberType.GENERAL)
                .role(MemberRole.ADMIN)
                .build();

        memberRepository.save(member);
    }
}
