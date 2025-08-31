package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.JwtPayload;
import reactor.core.publisher.Mono;

public interface JwtTokenGateway {
    Mono<String> generateToken(JwtPayload payload);
    Mono<JwtPayload> validateToken(String token);
    Mono<Boolean> isTokenExpired(String token);
}