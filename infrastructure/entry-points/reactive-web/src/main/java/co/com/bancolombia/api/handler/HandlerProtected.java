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

    public Mono<ServerResponse> getProfile(ServerRequest serverRequest) {
        return AuthenticationUtil.getAuthenticatedUser(serverRequest)
                .flatMap(user -> {
                    log.info("Usuario autenticado accediendo a perfil: {}", user.getEmail());
                    // Remover password antes de retornar
                    user.setPassword(null);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(user);
                });
    }

    public Mono<ServerResponse> updateProfile(ServerRequest serverRequest) {
        return AuthenticationUtil.getAuthenticatedUser(serverRequest)
                .flatMap(authenticatedUser -> {
                    log.info("Usuario {} actualizando perfil", authenticatedUser.getEmail());

                    // Aquí puedes agregar lógica para actualizar el perfil
                    // usando los casos de uso correspondientes

                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue("Perfil actualizado exitosamente");
                });
    }
}