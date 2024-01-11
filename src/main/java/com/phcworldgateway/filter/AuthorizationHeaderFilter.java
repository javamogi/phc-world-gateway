package com.phcworldgateway.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    @Override
    public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "no authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authorizationHeader.replace("Bearer ", "");

            try {
                String secret = env.getProperty("jwt.secret");
                String keyBase64Encoded = Base64.getEncoder().encodeToString(secret.getBytes());
                byte[] keyBytes = Decoders.BASE64.decode(keyBase64Encoded);
                Key key = Keys.hmacShaKeyFor(keyBytes);
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
                return onError(exchange, "잘못된 JWT 서명입니다.", HttpStatus.BAD_REQUEST);
            } catch (ExpiredJwtException e) {
                return onError(exchange, "만료된 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED);
            } catch (UnsupportedJwtException e) {
                return onError(exchange, "지원되지 않는 JWT 토큰입니다.", HttpStatus.BAD_REQUEST);
            } catch (IllegalArgumentException e) {
                return onError(exchange, "잘못된 토큰입니다.", HttpStatus.BAD_REQUEST);
            }

            return chain.filter(exchange);
        };
    }

    // Mono 단일, Flux 다수 -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }

    public static class Config {

    }
}
