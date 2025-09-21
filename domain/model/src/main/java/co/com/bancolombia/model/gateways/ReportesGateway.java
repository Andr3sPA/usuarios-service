package co.com.bancolombia.model.gateways;

import reactor.core.publisher.Mono;

public interface ReportesGateway {
    <R> Mono<R> getReportes(Class<R> responseType);
}
