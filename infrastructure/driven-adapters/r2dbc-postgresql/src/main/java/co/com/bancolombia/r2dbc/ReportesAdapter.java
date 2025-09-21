package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.gateways.ReportesGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ReportesAdapter implements ReportesGateway {
    private final WebClient webClient;
    private final String reportesBaseUrl;

    public ReportesAdapter(WebClient webClient,
                            @Value("${microservices.reportes.base-url:http://localhost:8082}") String reportesBaseUrl) {
        this.webClient = webClient;
        this.reportesBaseUrl = reportesBaseUrl;
    }
    @Override
    public <R> Mono<R> getReportes(Class<R> responseType){
        return webClient.get()
                .uri(reportesBaseUrl + "/api/v1/reportes")
                .retrieve()
                .bodyToMono(responseType)
                .doOnSuccess(response -> log.info("Respuesta recibida del servicio de reportes"))
                .doOnError(error -> log.error("Error al llamar al servicio de reportes", error));
    }
}
