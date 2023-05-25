package com.bettervns.apigateway;

import com.bettervns.configs.JwtValidationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

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
                        .uri("lb://bettervnssecurity"))
                .build();
    }
}