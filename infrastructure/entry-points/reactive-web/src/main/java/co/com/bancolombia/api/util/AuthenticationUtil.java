package co.com.bancolombia.api.util;
import co.com.bancolombia.dto.SessionResponse;
import co.com.bancolombia.exception.UserNotAuthenticatedException;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import co.com.bancolombia.model.User;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.List;
@Component
@RequiredArgsConstructor
public class AuthenticationUtil {
    private final ObjectMapper mapper;

    public Mono<SessionResponse> getAuthenticatedUser(ServerRequest request) {
        return Mono.justOrEmpty(request.attribute("authenticatedUser"))
                .cast(User.class)
                .map(user -> mapper.map(user, SessionResponse.class))
                .switchIfEmpty(Mono.error(new UserNotAuthenticatedException()));
    }
}
