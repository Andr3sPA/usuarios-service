package co.com.bancolombia.api.filter;

import co.com.bancolombia.api.config.UserPath;
import co.com.bancolombia.api.response.ErrorResponse;
import co.com.bancolombia.exception.BaseException;
import co.com.bancolombia.exception.InsufficientPrivilegesException;
import co.com.bancolombia.model.User;
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
public class RoleAuthorizationFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {


    private final List<String> adminOnlyPaths = List.of(
            "/api/v1/usuarios"
    );


    @Override
    @NonNull
    public Mono<ServerResponse> filter(@NonNull ServerRequest request,
                                       @NonNull HandlerFunction<ServerResponse> next) {

        String requestPath = request.path();


        if (requiresAdminRole(requestPath)) {
            return validateAdminRole(request)
                    .then(next.handle(request))
                    .onErrorResume(BaseException.class, this::handleAuthorizationError);
        }

        return next.handle(request);
    }

    private boolean requiresAdminRole(String path) {
        return adminOnlyPaths.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> validateAdminRole(ServerRequest request) {
        return Mono.justOrEmpty(request.attribute("authenticatedUser"))
                .cast(User.class)
                .flatMap(user -> {
                    String userRole = user.getRole().getName();
                    if (!"ADMIN".equals(userRole)) {
                        log.warn("Usuario {} con rol {} intentó acceder a recurso de admin",
                                user.getEmail(), userRole);
                        return Mono.error(new InsufficientPrivilegesException("ADMIN"));
                    }
                    log.info("Acceso autorizado para admin: {}", user.getEmail());
                    return Mono.empty();
                });
    }

    private Mono<ServerResponse> handleAuthorizationError(BaseException ex) {
        log.warn("Error de autorización: {}", ex.getMessage());
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