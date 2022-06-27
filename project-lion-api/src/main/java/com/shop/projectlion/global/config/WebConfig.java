package com.shop.projectlion.global.config;

import com.shop.projectlion.global.interceptor.ApiAuthenticationInterceptor;
import com.shop.projectlion.global.interceptor.ApiAuthorizationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ApiAuthenticationInterceptor apiAuthenticationInterceptor;
    private final ApiAuthorizationInterceptor apiAuthorizationInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name()
                );
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiAuthenticationInterceptor)
                .order(1)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/health", "/api/oauth/login", "/api/logout", "/api/token");

        registry.addInterceptor(apiAuthorizationInterceptor)
                .order(2)
                .addPathPatterns("/api/admin/**");
    }

}
