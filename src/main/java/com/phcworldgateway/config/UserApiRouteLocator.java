package com.phcworldgateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class UserApiRouteLocator {

    @Value("${route.user-api.v1.base-url}")
    private String userBaseUrl;

    private final String gatewayPath = "/providers/user-api/";

    @Bean
    public RouteLocator userRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("user-api-service",
                        r -> r.method(HttpMethod.POST)
                                .and()
                                .path(gatewayPath + "users/login")
                                .filters(f ->
                                    f.rewritePath(gatewayPath + "(?<servicePath>.*)", "/${servicePath}")
                                            .removeResponseHeader("Cookie"))
                                .uri(userBaseUrl))
                .route("user-api-service",
                        r -> r.method(HttpMethod.POST)
                                .and()
                                .path(gatewayPath + "users")
                                .filters(f ->
                                        f.rewritePath(gatewayPath + "(?<servicePath>.*)", "/${servicePath}")
                                                .removeResponseHeader("Cookie"))
                                .uri(userBaseUrl))
                .route("user-api-service",
                        r -> r.path(gatewayPath + "users/**")
                                .filters(f ->
                                        f.rewritePath(gatewayPath + "(?<servicePath>.*)", "/${servicePath}"))
                                .uri(userBaseUrl))
                .route("user-api-service",
                        r -> r.path(gatewayPath + "actuator/**")
                                .and()
                                .method(HttpMethod.GET, HttpMethod.POST)
                                .filters(f -> f.rewritePath(gatewayPath + "(?<servicePath>.*)", "/${servicePath}")
                                        .removeResponseHeader("Cookie"))
                                .uri(userBaseUrl))
                .build();
    }

}
