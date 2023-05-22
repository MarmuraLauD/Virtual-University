package com.bettervns.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("adminservice", r -> r.path("/admin", "/admin/**")
                        .filters(f -> f.filter(new JwtValidationFilter("")))
                        .uri("lb://adminservice"))
                .route("studentsservice", r -> r.path("/students", "/students/**", "/student", "/student/**")
                        .filters(f -> f.filter(new JwtValidationFilter("ROLE_STUDENT")))
                        .uri("lb://studentsservice"))
                .route("studyingservice", r -> r.path("/studying", "/studying/**")
                        .filters(f -> f.filter(new JwtValidationFilter("")))
                        .uri("lb://studyingservice"))
                .route("bettervnssecurity", r -> r.path( "/api/auth/**")
                        .filters(f -> f
                                .filter(new JwtValidationFilter("ROLE_ANYONE")))
                        .uri("lb://bettervnssecurity"))
                .build();
    }
}
