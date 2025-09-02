package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.util.AuthenticationUtil;
import co.com.bancolombia.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class HandlerProtected {
    private final AuthenticationUtil authenticationUtil;
    public Mono<ServerResponse> getProfile(ServerRequest serverRequest) {
        return authenticationUtil.getAuthenticatedUser(serverRequest) // ✅ ahora es de instancia
                .flatMap(user -> {
                    log.info("Usuario autenticado accediendo a perfil: {}", user.getEmail());

                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(user);
                });
    }

}