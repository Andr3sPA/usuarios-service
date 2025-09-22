package co.com.bancolombia.api.handler;

import org.junit.jupiter.api.Test;



import co.com.bancolombia.api.util.AuthenticationUtil;
import co.com.bancolombia.usecase.reportes.ReportesUseCase;
import org.reactivecommons.utils.ObjectMapper;
import org.mockito.Mockito;

class HandlerReporteTest {
    @Test
    void testConstructor() {
        AuthenticationUtil authenticationUtil = Mockito.mock(AuthenticationUtil.class);
        ObjectMapper mapper = Mockito.mock(ObjectMapper.class);
        ReportesUseCase reportesUseCase = Mockito.mock(ReportesUseCase.class);
        HandlerReporte handler = new HandlerReporte(authenticationUtil, mapper, reportesUseCase);
        assert handler != null;
    }

    @Test
    void testGetReportesSuccess() {
        AuthenticationUtil authenticationUtil = Mockito.mock(AuthenticationUtil.class);
        ObjectMapper mapper = Mockito.mock(ObjectMapper.class);
        ReportesUseCase reportesUseCase = Mockito.mock(ReportesUseCase.class);
        HandlerReporte handler = new HandlerReporte(authenticationUtil, mapper, reportesUseCase);
        org.springframework.web.reactive.function.server.ServerRequest request = Mockito.mock(org.springframework.web.reactive.function.server.ServerRequest.class);
        com.fasterxml.jackson.databind.JsonNode reports = Mockito.mock(com.fasterxml.jackson.databind.JsonNode.class);
        Mockito.when(reportesUseCase.getReportes(com.fasterxml.jackson.databind.JsonNode.class)).thenReturn(reactor.core.publisher.Mono.just(reports));
        var result = handler.getReportes(request).block();
        assert result != null;
    }

    @Test
    void testGetReportesEmpty() {
    AuthenticationUtil authenticationUtil = Mockito.mock(AuthenticationUtil.class);
    ObjectMapper mapper = Mockito.mock(ObjectMapper.class);
    ReportesUseCase reportesUseCase = Mockito.mock(ReportesUseCase.class);
    HandlerReporte handler = new HandlerReporte(authenticationUtil, mapper, reportesUseCase);
    org.springframework.web.reactive.function.server.ServerRequest request = Mockito.mock(org.springframework.web.reactive.function.server.ServerRequest.class);
    Mockito.when(reportesUseCase.getReportes(com.fasterxml.jackson.databind.JsonNode.class)).thenReturn(reactor.core.publisher.Mono.empty());
    var response = handler.getReportes(request).block();
    assert response != null;
    assert response.statusCode().value() == 204;
    }
}
