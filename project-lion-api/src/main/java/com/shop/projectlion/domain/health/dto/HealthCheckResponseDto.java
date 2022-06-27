package com.shop.projectlion.domain.health.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HealthCheckResponseDto {

    private boolean status;
    private String health;

    public static HealthCheckResponseDto getHealthDtoByIsHealth(boolean isHealth) {
        String health = isHealth ? "ok" : "no";

        return HealthCheckResponseDto.builder()
                .status(isHealth)
                .health(health)
                .build();
    }

}
