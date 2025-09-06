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
        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("0"));
        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));
        return authenticationUtil.getAuthenticatedUser(serverRequest)
                .flatMap(user ->
                        solicitudUseCase.getSolicitudes(
                                JsonNode.class,
                                page,
                                size
                        )
                )
                .flatMap(result -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(result))
                .doOnSuccess(response -> log.info("Solicitudes consultadas exitosamente"))
                .doOnError(error -> log.error("Error al consultar solicitudes", error));
    }
    public Mono<ServerResponse> updateSolicitud(ServerRequest req){
        return authenticationUtil.getAuthenticatedUser(req)
                .flatMap(user ->
                        req.bodyToMono(JsonNode.class)
                                .flatMap(solicitudData ->
                                        solicitudUseCase.updateSolicitud(
                                                solicitudData,
                                                JsonNode.class
                                        )
                                )
                )
                .flatMap(result -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(result))
                .doOnSuccess(response -> log.info("Solicitud actualizada exitosamente"))
                .doOnError(error -> log.error("Error al actualizar solicitud", error));

    }

}
