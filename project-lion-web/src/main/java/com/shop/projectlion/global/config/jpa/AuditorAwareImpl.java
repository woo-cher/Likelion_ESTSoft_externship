package com.shop.projectlion.global.config.jpa;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (StringUtils.equals(authentication.getName(), "anonymousUser")) {
            return Optional.of("NOT_EXIST");
        }

        return Optional.of(authentication.getName());
    }
}
