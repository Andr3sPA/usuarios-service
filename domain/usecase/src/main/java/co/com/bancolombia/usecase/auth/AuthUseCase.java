package co.com.bancolombia.usecase.auth;

import co.com.bancolombia.exception.BaseException;
import co.com.bancolombia.model.JwtPayload;
import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.JwtTokenGateway;
import co.com.bancolombia.model.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
public class AuthUseCase {

    private final UserRepository userRepository;
    private final JwtTokenGateway jwtTokenGateway;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Mono<String> login(String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new BaseException(
                        "INVALID_CREDENTIALS",
                        "Credenciales inválidas",
                        "Email o contraseña incorrectos",
                        List.of("Usuario no encontrado"),
                        401
                )))
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .switchIfEmpty(Mono.error(new BaseException(
                        "INVALID_CREDENTIALS",
                        "Credenciales inválidas",
                        "Email o contraseña incorrectos",
                        List.of("Contraseña incorrecta"),
                        401
                )))
                .flatMap(this::generateTokenForUser);
    }

    public Mono<User> validateTokenAndGetUser(String token) {
        return jwtTokenGateway.validateToken(token)
                .flatMap(payload -> userRepository.findByEmail(payload.getSubject()))
                .onErrorMap(ex -> new BaseException(
                        "INVALID_TOKEN",
                        "Token inválido",
                        "El token proporcionado no es válido o ha expirado",
                        List.of("Token no válido: " + ex.getMessage()),
                        401
                ));
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
