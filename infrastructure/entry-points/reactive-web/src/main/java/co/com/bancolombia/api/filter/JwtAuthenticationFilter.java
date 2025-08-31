package co.com.bancolombia.api.filter;

import co.com.bancolombia.api.response.ErrorResponse;
import co.com.bancolombia.exception.BaseException;
import co.com.bancolombia.usecase.auth.AuthUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final AuthUseCase authUseCase;

    @Override
    @NonNull
    public Mono<ServerResponse> filter(@NonNull ServerRequest request,
                                       @NonNull HandlerFunction<ServerResponse> next) {

        // Rutas que no requieren autenticación
        String path = request.path();
        if (isPublicPath(path)) {
            return next.handle(request);
        }

        return extractTokenFromCookies(request)
                .flatMap(authUseCase::validateTokenAndGetUser)
                .flatMap(user -> {
                    // Agregar el usuario al contexto de la request
                    ServerRequest enrichedRequest = ServerRequest.from(request)
                            .attribute("authenticatedUser", user)
                            .build();
                    return next.handle(enrichedRequest);
                })
                .onErrorResume(BaseException.class, ex -> {
                    log.warn("Error de autenticación: {}", ex.getMessage());
                    return ServerResponse.status(ex.getStatus())
                            .bodyValue(ErrorResponse.builder()
                                    .errorCode(ex.getErrorCode())
                                    .tittle(ex.getTitle())
                                    .message(ex.getMessage())
                                    .errors(ex.getErrors())
                                    .status(ex.getStatus())
                                    .timestamp(ex.getTimestamp())
                                    .build());
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("Error inesperado en filtro JWT", ex);
                    return ServerResponse.status(401)
                            .bodyValue(ErrorResponse.builder()
                                    .errorCode("AUTHENTICATION_ERROR")
                                    .tittle("Error de autenticación")
                                    .message("No se pudo validar la autenticación")
                                    .errors(List.of("Error interno del servidor"))
                                    .status(401)
                                    .timestamp(Instant.now())
                                    .build());
                });
    }

    private boolean isPublicPath(String path) {
        return path.equals("/api/v1/usuarios") ||
                path.equals("/api/v1/auth/login") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/webjars");
    }

    private Mono<String> extractTokenFromCookies(ServerRequest request) {
        return Mono.justOrEmpty(request.cookies().getFirst("jwt-token"))
                .map(cookie -> cookie.getValue())
                .switchIfEmpty(Mono.error(new BaseException(
                        "TOKEN_NOT_FOUND",
                        "Token no encontrado",
                        "No se encontró token de autenticación en las cookies",
                        List.of("Cookie jwt-token no presente"),
                        401
                )));
    }
}