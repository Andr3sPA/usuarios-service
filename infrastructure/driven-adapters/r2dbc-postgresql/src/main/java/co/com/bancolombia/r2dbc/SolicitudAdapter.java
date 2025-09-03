package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.SolicitudGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SolicitudAdapter implements SolicitudGateway {

    private final WebClient webClient;

    @Value("${microservices.solicitud.base-url:http://localhost:8081}")
    private String solicitudBaseUrl;
    @Override
    public <T, R> Mono<R> createSolicitud(T solicitudData, User authenticatedUser, Class<R> responseType) {
        log.info("Creando solicitud para usuario: {}", authenticatedUser.getEmail());

        return webClient.post()
                .uri(solicitudBaseUrl + "/api/v1/solicitud")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-User-Email", authenticatedUser.getEmail())
                .header("X-User-Id", authenticatedUser.getId().toString())
                .header("X-User-Role", authenticatedUser.getRole().getName())
                .bodyValue(solicitudData)
                .retrieve()
                .bodyToMono(responseType)
                .doOnSuccess(response -> log.info("Solicitud creada exitosamente para usuario: {}",
                        authenticatedUser.getEmail()))
                .doOnError(error -> log.error("Error al crear solicitud para usuario: {}",
                        authenticatedUser.getEmail(), error));
    }

    @Override
    public <R> Mono<R> getSolicitudes(Class<R> responseType) {
        return webClient.get()
                .uri(solicitudBaseUrl + "/api/v1/solicitud")
                .retrieve()
                .bodyToMono(responseType)
                .doOnSuccess(response -> log.info("Solicitud {} obtenida exitosamente"))
                .doOnError(error -> log.error("Error al obtener solicitud {}", error));
    }

}