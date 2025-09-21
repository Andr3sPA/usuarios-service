package co.com.bancolombia.usecase.reportes;

import co.com.bancolombia.model.gateways.ReportesGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReportesUseCase {
    private final ReportesGateway reportesGateway;
    public <R> Mono<R> getReportes(Class<R> responseType) {
        return reportesGateway.getReportes(responseType);
    }
}
