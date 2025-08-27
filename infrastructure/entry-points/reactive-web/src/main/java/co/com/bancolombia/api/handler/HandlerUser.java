package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.dto.UserRegisterRequest;
import co.com.bancolombia.usecase.task.UserUseCase;
import co.com.bancolombia.r2dbc.mapper.UserRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class HandlerUser {

    private final UserUseCase userUseCase;
    private final UserRequestMapper userRequestMapper;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> registerUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserRegisterRequest.class)
                .flatMap(dto -> {
                    requestValidator.validate(dto, UserRegisterRequest.class);
                    return Mono.just(dto);
                })
                .map(userRequestMapper::toModel)
                .flatMap(userUseCase::register)
                .flatMap(result -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(result));
    }

}

