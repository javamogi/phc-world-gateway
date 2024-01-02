package com.phcworldgateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserApiRouteLocator {

    @Value("${route.user-api.v1.base-url}")
    private String userBaseUrl;

    private final String gatewayPath = "/providers/user-api/";

    @Bean
    public RouteLocator userRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("user-api",
                        r -> r.path(gatewayPath + "**")
                        .filters(f ->
                                    f.rewritePath(gatewayPath + "(?<servicePath>.*)", "/${servicePath}"))
                        .uri(userBaseUrl))
                .build();
    }
}
