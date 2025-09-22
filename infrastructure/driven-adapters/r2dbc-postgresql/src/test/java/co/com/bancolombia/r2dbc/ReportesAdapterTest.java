package co.com.bancolombia.r2dbc;

import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

class ReportesAdapterTest {
    @Test
    void getReportes_shouldReturnMonoSuccess() {
        WebClient webClient = org.mockito.Mockito.mock(WebClient.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
        String baseUrl = "http://localhost:8082";
        ReportesAdapter adapter = new ReportesAdapter(webClient, baseUrl);

    WebClient.RequestHeadersUriSpec uriSpec = org.mockito.Mockito.mock(WebClient.RequestHeadersUriSpec.class);
    WebClient.RequestHeadersSpec headersSpec = org.mockito.Mockito.mock(WebClient.RequestHeadersSpec.class);
    WebClient.ResponseSpec responseSpec = org.mockito.Mockito.mock(WebClient.ResponseSpec.class);
    org.mockito.Mockito.when(webClient.get()).thenReturn(uriSpec);
    org.mockito.Mockito.when(uriSpec.uri(baseUrl + "/api/v1/reportes")).thenReturn(headersSpec);
    org.mockito.Mockito.when(headersSpec.retrieve()).thenReturn(responseSpec);
    org.mockito.Mockito.when(responseSpec.bodyToMono(String.class)).thenReturn(reactor.core.publisher.Mono.just("ok"));

        reactor.core.publisher.Mono<String> result = adapter.getReportes(String.class);
        org.junit.jupiter.api.Assertions.assertEquals("ok", result.block());
    }

    @Test
    void getReportes_shouldReturnMonoEmpty() {
        WebClient webClient = org.mockito.Mockito.mock(WebClient.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
        String baseUrl = "http://localhost:8082";
        ReportesAdapter adapter = new ReportesAdapter(webClient, baseUrl);

    WebClient.RequestHeadersUriSpec uriSpec = org.mockito.Mockito.mock(WebClient.RequestHeadersUriSpec.class);
    WebClient.RequestHeadersSpec headersSpec = org.mockito.Mockito.mock(WebClient.RequestHeadersSpec.class);
    WebClient.ResponseSpec responseSpec = org.mockito.Mockito.mock(WebClient.ResponseSpec.class);
    org.mockito.Mockito.when(webClient.get()).thenReturn(uriSpec);
    org.mockito.Mockito.when(uriSpec.uri(baseUrl + "/api/v1/reportes")).thenReturn(headersSpec);
    org.mockito.Mockito.when(headersSpec.retrieve()).thenReturn(responseSpec);
    org.mockito.Mockito.when(responseSpec.bodyToMono(String.class)).thenReturn(reactor.core.publisher.Mono.empty());

        reactor.core.publisher.Mono<String> result = adapter.getReportes(String.class);
        org.junit.jupiter.api.Assertions.assertNull(result.block());
    }

    @Test
    void getReportes_shouldHandleError() {
        WebClient webClient = org.mockito.Mockito.mock(WebClient.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
        String baseUrl = "http://localhost:8082";
        ReportesAdapter adapter = new ReportesAdapter(webClient, baseUrl);

    WebClient.RequestHeadersUriSpec uriSpec = org.mockito.Mockito.mock(WebClient.RequestHeadersUriSpec.class);
    WebClient.RequestHeadersSpec headersSpec = org.mockito.Mockito.mock(WebClient.RequestHeadersSpec.class);
    WebClient.ResponseSpec responseSpec = org.mockito.Mockito.mock(WebClient.ResponseSpec.class);
    org.mockito.Mockito.when(webClient.get()).thenReturn(uriSpec);
    org.mockito.Mockito.when(uriSpec.uri(baseUrl + "/api/v1/reportes")).thenReturn(headersSpec);
    org.mockito.Mockito.when(headersSpec.retrieve()).thenReturn(responseSpec);
    org.mockito.Mockito.when(responseSpec.bodyToMono(String.class)).thenReturn(reactor.core.publisher.Mono.error(new RuntimeException("error")));

        reactor.core.publisher.Mono<String> result = adapter.getReportes(String.class);
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, result::block);
    }
    @Test
    void constructor_shouldCreateInstance() {
        WebClient webClient = WebClient.builder().build();
        String baseUrl = "http://localhost:8082";
        ReportesAdapter adapter = new ReportesAdapter(webClient, baseUrl);
        assert adapter != null;
    }
}
