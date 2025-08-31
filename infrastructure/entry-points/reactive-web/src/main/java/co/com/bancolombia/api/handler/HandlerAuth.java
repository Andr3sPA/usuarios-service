package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.dto.LoginRequest;
import co.com.bancolombia.dto.LoginResponse;
import co.com.bancolombia.usecase.auth.AuthUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class HandlerAuth {

    private final AuthUseCase authUseCase;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequest.class)
                .flatMap(dto -> {
                    requestValidator.validate(dto, LoginRequest.class);
                    return Mono.just(dto);
                })
                .flatMap(loginRequest ->
                        authUseCase.login(loginRequest.getEmail(), loginRequest.getPassword())
                                .map(token -> {
                                    ResponseCookie jwtCookie = ResponseCookie.from("jwt-token", token)
                                            .httpOnly(true)
                                            .secure(true) // Solo en HTTPS
                                            .sameSite("Strict")
                                            .maxAge(Duration.ofHours(24))
                                            .path("/")
                                            .build();

                                    LoginResponse response = LoginResponse.builder()
                                            .message("Login exitoso")
                                            .userEmail(loginRequest.getEmail())
                                            .role("USER") // Esto debería venir del usuario autenticado
                                            .build();

                                    return ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .cookie(jwtCookie)
                                            .bodyValue(response);
                                })
                )
                .flatMap(responseBuilder -> responseBuilder)
                .doOnSuccess(response -> log.info("Login exitoso"))
                .doOnError(error -> log.error("Error en login", error));
    }

    public Mono<ServerResponse> logout(ServerRequest serverRequest) {
        ResponseCookie expiredCookie = ResponseCookie.from("jwt-token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(Duration.ZERO)
                .path("/")
                .build();

        return ServerResponse.ok()
                .cookie(expiredCookie)
                .bodyValue("Logout exitoso");
    }
}