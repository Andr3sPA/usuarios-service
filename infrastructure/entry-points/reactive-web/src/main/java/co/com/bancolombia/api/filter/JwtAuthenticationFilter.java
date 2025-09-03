package co.com.bancolombia.api.filter;

import co.com.bancolombia.api.config.UserPath;
import co.com.bancolombia.api.response.ErrorResponse;
import co.com.bancolombia.exception.AuthenticationException;
import co.com.bancolombia.exception.BaseException;
import co.com.bancolombia.exception.InsufficientPrivilegesException;
import co.com.bancolombia.exception.TokenNotFoundException;
import co.com.bancolombia.model.User;
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

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final AuthUseCase authUseCase;
    private final UserPath userPath;

    // Rutas que requieren rol de ADMIN
    private final List<String> adminOnlyPaths = List.of(
            "/api/v1/usuarios"
    );

    @Override
    @NonNull
    public Mono<ServerResponse> filter(@NonNull ServerRequest request,
                                       @NonNull HandlerFunction<ServerResponse> next) {

        String requestPath = request.path();
        log.info("=== JWT AUTH FILTER para ruta: {} ===", requestPath);

        // Rutas públicas - no requieren autenticación
        List<String> publicPaths = List.of(userPath.getLogin());

        if (publicPaths.contains(requestPath)) {
            log.info("Ruta pública: {}, saltando autenticación", requestPath);
            return next.handle(request);
        }

        // Rutas protegidas - requieren autenticación
        log.info("Ruta protegida: {}, validando autenticación", requestPath);

        return extractTokenFromCookies(request)
                .flatMap(authUseCase::validateTokenAndGetUser)
                .flatMap(user -> {
                    log.info("Usuario autenticado: {} con rol: {}", user.getEmail(), user.getRole().getName());

                    // Validar rol si es necesario
                    if (requiresAdminRole(requestPath)) {
                        return validateAdminRole(user, requestPath)
                                .then(proceedWithRequest(request, user, next));
                    } else {
                        // Solo autenticación requerida
                        return proceedWithRequest(request, user, next);
                    }
                })
                .onErrorResume(BaseException.class, this::handleError)
                .onErrorResume(Exception.class, ex -> {
                    log.error("Error inesperado en filtro JWT", ex);
                    return Mono.error(new AuthenticationException(ex.getMessage()));
                });
    }

    private boolean requiresAdminRole(String path) {
        return adminOnlyPaths.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> validateAdminRole(User user, String requestPath) {
        String userRole = user.getRole().getName();

        if (!"ADMIN".equals(userRole)) {
            log.warn("Usuario {} con rol {} intentó acceder a recurso de admin: {}",
                    user.getEmail(), userRole, requestPath);
            return Mono.error(new InsufficientPrivilegesException("ADMIN"));
        }

        log.info("Acceso autorizado para admin: {} en {}", user.getEmail(), requestPath);
        return Mono.empty();
    }

    private Mono<ServerResponse> proceedWithRequest(ServerRequest request, User user, HandlerFunction<ServerResponse> next) {
        // ✅ No se reconstruye el ServerRequest (no se pierde el body)
        request.attributes().put("authenticatedUser", user);
        log.info("Procesando request para usuario: {}", user.getEmail());
        return next.handle(request);
    }

    private Mono<String> extractTokenFromCookies(ServerRequest request) {
        return Mono.justOrEmpty(request.cookies().getFirst("jwt-token"))
                .map(cookie -> cookie.getValue())
                .switchIfEmpty(Mono.error(new TokenNotFoundException()));
    }

    private Mono<ServerResponse> handleError(BaseException ex) {
        log.warn("Error de autenticación/autorización: {} - {}", ex.getErrorCode(), ex.getMessage());
        return ServerResponse.status(ex.getStatus())
                .bodyValue(ErrorResponse.builder()
                        .errorCode(ex.getErrorCode())
                        .tittle(ex.getTitle())
                        .message(ex.getMessage())
                        .errors(ex.getErrors())
                        .status(ex.getStatus())
                        .timestamp(ex.getTimestamp())
                        .build());
    }
}
