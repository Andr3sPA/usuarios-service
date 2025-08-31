package co.com.bancolombia.jwt;

import co.com.bancolombia.model.JwtPayload;
import co.com.bancolombia.model.gateways.JwtTokenGateway;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenAdapter implements JwtTokenGateway {

    private final SecretKey secretKey;
    private final long expirationTimeMillis;

    public JwtTokenAdapter(@Value("${jwt.secret:defaultSecretKeyThatShouldBeChanged}") String secret,
                           @Value("${jwt.expiration:86400000}") long expirationTimeMillis) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationTimeMillis = expirationTimeMillis;
    }

    @Override
    public Mono<String> generateToken(JwtPayload payload) {
        return Mono.fromCallable(() -> {
            try {
                String token = Jwts.builder()
                        .subject(payload.getSubject())
                        .claim("userId", payload.getUserId())
                        .claim("role", payload.getRole())
                        .issuedAt(Date.from(payload.getIssuedAt()))
                        .expiration(Date.from(payload.getExpiresAt()))
                        .signWith(secretKey)
                        .compact();

                log.info("Token generado exitosamente para usuario: {}", payload.getSubject());
                return token;
            } catch (Exception e) {
                log.error("Error generando token para usuario: {}", payload.getSubject(), e);
                throw new RuntimeException("Error generando token JWT", e);
            }
        });
    }

    @Override
    public Mono<JwtPayload> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                JwtPayload payload = JwtPayload.builder()
                        .subject(claims.getSubject())
                        .userId(claims.get("userId", Long.class))
                        .role(claims.get("role", String.class))
                        .issuedAt(claims.getIssuedAt().toInstant())
                        .expiresAt(claims.getExpiration().toInstant())
                        .build();

                log.info("Token validado exitosamente para usuario: {}", payload.getSubject());
                return payload;
            } catch (Exception e) {
                log.error("Error validando token", e);
                throw new RuntimeException("Token inválido", e);
            }
        });
    }

    @Override
    public Mono<Boolean> isTokenExpired(String token) {
        return validateToken(token)
                .map(payload -> Instant.now().isAfter(payload.getExpiresAt()))
                .onErrorReturn(true);
    }
}