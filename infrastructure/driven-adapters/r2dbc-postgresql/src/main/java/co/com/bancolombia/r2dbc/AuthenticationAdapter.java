package co.com.bancolombia.r2dbc;

import co.com.bancolombia.exception.InvalidCredentialsException;
import co.com.bancolombia.exception.InvalidTokenException;
import co.com.bancolombia.model.JwtPayload;
import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.AuthenticationGateway;
import co.com.bancolombia.model.gateways.JwtTokenGateway;
import co.com.bancolombia.model.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationAdapter implements AuthenticationGateway {

    private final UserGateway userGateway;
    private final JwtTokenGateway jwtTokenGateway;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public Mono<String> authenticateUser(String email, String password) {
        log.info("Intentando autenticar usuario: {}", email);

        return userGateway.findByEmail(email)
                .switchIfEmpty(Mono.error(InvalidCredentialsException::new))
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .switchIfEmpty(Mono.error(InvalidCredentialsException::new))
                .flatMap(this::generateTokenForUser);
    }
    public Mono<User> validateTokenAndGetUser(String token) {
        return jwtTokenGateway.validateToken(token)
                .flatMap(payload -> userGateway.findByEmail(payload.getSubject()))
                .onErrorMap(ex -> new InvalidTokenException(ex.getMessage()));

    }

    private Mono<String> generateTokenForUser(User user) {
        JwtPayload payload = JwtPayload.builder()
                .subject(user.getEmail())
                .userId(user.getId())
                .role(user.getRole().getName())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .build();

        return jwtTokenGateway.generateToken(payload);
    }
}