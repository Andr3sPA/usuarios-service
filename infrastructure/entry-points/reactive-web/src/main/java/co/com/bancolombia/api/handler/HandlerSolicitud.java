package co.com.bancolombia.api.handler;
import co.com.bancolombia.model.User;
import org.reactivecommons.utils.ObjectMapper;
import co.com.bancolombia.api.util.AuthenticationUtil;
import co.com.bancolombia.dto.SessionResponse;
import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import com.fasterxml.jackson.databind.JsonNode;
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
public class HandlerSolicitud {

    private final SolicitudUseCase solicitudUseCase;
    private final AuthenticationUtil authenticationUtil;
    private final ObjectMapper mapper;
    public Mono<ServerResponse> createSolicitud(ServerRequest serverRequest) {
        return authenticationUtil.getAuthenticatedUser(serverRequest)
                .flatMap(user ->
                        serverRequest.bodyToMono(JsonNode.class)
                                .flatMap(solicitudData ->
                                        solicitudUseCase.createSolicitud(
                                                solicitudData,
                                                mapper.map(user, User.class),
                                                JsonNode.class
                                        )
                                )
                )
                .flatMap(result -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(result))
                .doOnSuccess(response -> log.info("Solicitud creada exitosamente"))
                .doOnError(error -> log.error("Error al crear solicitud", error));
    }

    public Mono<ServerResponse> getSolicitudes(ServerRequest serverRequest) {

        return authenticationUtil.getAuthenticatedUser(serverRequest)
                .flatMap(user ->
                        solicitudUseCase.getSolicitudes(
                                JsonNode.class
                        )
                )
                .flatMap(result -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(result))
                .doOnSuccess(response -> log.info("Solicitudes consultadas exitosamente"))
                .doOnError(error -> log.error("Error al consultar solicitudes", error));
    }

}
