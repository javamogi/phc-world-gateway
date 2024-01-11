package com.phcworldgateway.config;

import com.phcworldgateway.filter.AuthorizationHeaderFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class FreeBoardAnswerApiRouteLocator {

    @Value("${route.answer-api.v1.base-url}")
    private String userBaseUrl;

    private final String gatewayPath = "/providers/answer-api/";

    @Bean
    public RouteLocator boardAnswerRouteLocator(RouteLocatorBuilder builder, AuthorizationHeaderFilter authorizationHeaderFilter){
        return builder.routes()
                .route("answer-api-service",
                        r -> r.path(gatewayPath + "answers/**")
                                .filters(f ->
                                        f.rewritePath(gatewayPath + "(?<servicePath>.*)", "/${servicePath}")
                                                .filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config()))
                                )
                                .uri(userBaseUrl))
                .route("answer-api-service",
                        r -> r.path(gatewayPath + "actuator/**")
                                .and()
                                .method(HttpMethod.GET, HttpMethod.POST)
                                .filters(f -> f.rewritePath(gatewayPath + "(?<servicePath>.*)", "/${servicePath}")
                                        .removeResponseHeader("Cookie"))
                                .uri(userBaseUrl))
                .build();
    }

}
