package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.util.AuthenticationUtil;
import co.com.bancolombia.usecase.reportes.ReportesUseCase;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class HandlerReporte {
    private final AuthenticationUtil authenticationUtil;
    private final ObjectMapper mapper;
    private final ReportesUseCase reportesUseCase;
    public Mono<ServerResponse> getReportes(ServerRequest serverRequest){
        return reportesUseCase.getReportes(JsonNode.class)
                .flatMap(reports -> ServerResponse.ok()
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .bodyValue(reports))
                .switchIfEmpty(ServerResponse.noContent().build());
    }
}
