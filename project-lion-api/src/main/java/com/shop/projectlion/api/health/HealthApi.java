package com.shop.projectlion.api.health;

import com.shop.projectlion.domain.health.dto.HealthCheckResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

@RestController
public class HealthApi {

    @GetMapping("/api/health")
    public ResponseEntity<HealthCheckResponseDto> health() {

        try {
            return ResponseEntity.ok().body(HealthCheckResponseDto.getHealthDtoByIsHealth(true));
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(HealthCheckResponseDto.getHealthDtoByIsHealth(false));
        }
    }
}
