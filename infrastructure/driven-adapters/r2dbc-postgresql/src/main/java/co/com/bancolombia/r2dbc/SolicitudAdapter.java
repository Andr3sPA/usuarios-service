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
public class SolicitudAdapter implements SolicitudGateway {

    private final WebClient webClient;
    private final String solicitudBaseUrl;

    public SolicitudAdapter(WebClient webClient,
                            @Value("${microservices.solicitud.base-url:http://localhost:8081}") String solicitudBaseUrl) {
        this.webClient = webClient;
        this.solicitudBaseUrl = solicitudBaseUrl;
    }
        @Override
        public <T, R> Mono<R> createSolicitud(T solicitudData, User authenticatedUser, Class<R> responseType) {
                log.info("Creando solicitud para usuario: {}", authenticatedUser.getEmail());

                return webClient.post()
                                .uri(solicitudBaseUrl + "/api/v1/solicitud")
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .header("X-User-Id", authenticatedUser.getId().toString())
                                .header("X-User-Email", authenticatedUser.getEmail())
                                .header("X-User-Role", authenticatedUser.getRole().getName())
                                .bodyValue(solicitudData)
                                .exchangeToMono(response -> response.bodyToMono(responseType));
    }
    @Override
    public <T, R> Mono<R> calculateCapacity(T solicitudData, User authenticatedUser, Class<R> responseType) {
        log.info("Creando solicitud para usuario: {}", authenticatedUser.getEmail());

        return webClient.post()
                .uri(solicitudBaseUrl + "/api/v1/calcular-capacidad")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-User-Id", authenticatedUser.getId().toString())
                .header("X-User-Email", authenticatedUser.getEmail())
                .header("X-User-Role", authenticatedUser.getRole().getName())
                .header("X-User-Salary", String.valueOf(authenticatedUser.getBaseSalary()))
                .bodyValue(solicitudData)
                .exchangeToMono(response -> response.bodyToMono(responseType));
    }

        @Override
        public <R> Mono<R> getSolicitudes(Class<R> responseType,int page,int size) {
            String url = String.format("%s/api/v1/solicitud?page=%d&size=%d",
                    solicitudBaseUrl, page, size);
                return webClient.get()
                                .uri(url)
                                .exchangeToMono(response -> response.bodyToMono(responseType));
    }

    @Override
    public <T, R> Mono<R> updateSolicitud(T solicitudData, Class<R> responseType) {
        log.info("Actualizando solicitud");
        return webClient.put()
                .uri(solicitudBaseUrl + "/api/v1/solicitud")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(solicitudData)
                .exchangeToMono(response -> response.bodyToMono(responseType));
    }

}