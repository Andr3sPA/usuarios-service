package co.com.bancolombia.api.filter;

import co.com.bancolombia.api.config.UserPath;
import co.com.bancolombia.api.response.ErrorResponse;
import co.com.bancolombia.exception.AuthenticationException;
import co.com.bancolombia.exception.BaseException;
import co.com.bancolombia.exception.TokenNotFoundException;
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
    private final UserPath userPath;

    @Override
    @NonNull
    public Mono<ServerResponse> filter(@NonNull ServerRequest request,
                                       @NonNull HandlerFunction<ServerResponse> next) {

        String requestPath = request.path();
        List<String> publicPaths = List.of(userPath.getLogin());

        if (publicPaths.contains(requestPath)) {
            return next.handle(request);
        }


        return extractTokenFromCookies(request)
                .flatMap(authUseCase::validateTokenAndGetUser)
                .flatMap(user -> {
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
                    return Mono.error(new AuthenticationException(ex.getMessage()));
                });
    }


    private Mono<String> extractTokenFromCookies(ServerRequest request) {
        return Mono.justOrEmpty(request.cookies().getFirst("jwt-token"))
                .map(cookie -> cookie.getValue())
                .switchIfEmpty(Mono.error(new TokenNotFoundException()));
    }
}