package co.com.bancolombia.api.util;

import co.com.bancolombia.exception.BaseException;
import co.com.bancolombia.model.User;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public class AuthenticationUtil {

    public static Mono<User> getAuthenticatedUser(ServerRequest request) {
        return Mono.justOrEmpty(request.attribute("authenticatedUser"))
                .cast(User.class)
                .switchIfEmpty(Mono.error(new BaseException(
                        "USER_NOT_AUTHENTICATED",
                        "Usuario no autenticado",
                        "No se encontró información del usuario autenticado",
                        List.of("Usuario no presente en el contexto"),
                        401
                )));
    }
}