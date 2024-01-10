package com.phcworldgateway.config;

import com.phcworldgateway.filter.AuthorizationHeaderFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class FreeBoardApiRouteLocator {

    @Value("${route.board-api.v1.base-url}")
    private String userBaseUrl;

    private final String gatewayPath = "/providers/board-api/";

    @Bean
    public RouteLocator boardRouteLocator(RouteLocatorBuilder builder, AuthorizationHeaderFilter authorizationHeaderFilter){
        return builder.routes()
                .route("board-api-service",
                        r -> r.path(gatewayPath + "freeboards/**")
                                .filters(f ->
                                        f.rewritePath(gatewayPath + "(?<servicePath>.*)", "/${servicePath}")
                                                .filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config()))
                                )
                                .uri(userBaseUrl))
                .route("board-api-service",
                        r -> r.path(gatewayPath + "actuator/**")
                                .and()
                                .method(HttpMethod.GET, HttpMethod.POST)
                                .filters(f -> f.rewritePath(gatewayPath + "(?<servicePath>.*)", "/${servicePath}")
                                        .removeResponseHeader("Cookie"))
                                .uri(userBaseUrl))
                .build();
    }

}
