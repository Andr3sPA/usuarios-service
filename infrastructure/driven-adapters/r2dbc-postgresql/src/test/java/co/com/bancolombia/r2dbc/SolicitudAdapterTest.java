package co.com.bancolombia.r2dbc;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import co.com.bancolombia.model.User;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SolicitudAdapterTest {
    private WebClient webClient;
    private SolicitudAdapter adapter;

    @BeforeEach
    void setUp() {
        webClient = Mockito.mock(WebClient.class, Mockito.RETURNS_DEEP_STUBS);
        adapter = new SolicitudAdapter(webClient, "http://localhost");
    }

    @Test
    void constructor_shouldCreateInstance() {
        assert adapter != null;
    }



    @Test
    void createSolicitud_shouldReturnMono() {
        WebClient.RequestBodyUriSpec uriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = Mockito.mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec headersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
            when(webClient.post()).thenReturn(uriSpec);
            when(uriSpec.uri(any(String.class))).thenReturn(bodySpec);
            when(bodySpec.header(any(), any())).thenReturn(bodySpec);
            when(bodySpec.bodyValue(any())).thenReturn(headersSpec);
            when(headersSpec.exchangeToMono(any())).thenReturn(Mono.just("response"));

        User user = User.builder().id(1L).email("test@example.com").role(co.com.bancolombia.model.Role.builder().name("ADMIN").build()).build();
        Mono<String> result = adapter.createSolicitud(new Object(), user, String.class);
        assert result.block().equals("response");
    }



    @Test
    void calculateCapacity_shouldReturnMono() {
        WebClient.RequestBodyUriSpec uriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = Mockito.mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec headersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
            when(webClient.post()).thenReturn(uriSpec);
            when(uriSpec.uri(any(String.class))).thenReturn(bodySpec);
            when(bodySpec.header(any(), any())).thenReturn(bodySpec);
            when(bodySpec.bodyValue(any())).thenReturn(headersSpec);
            when(headersSpec.exchangeToMono(any())).thenReturn(Mono.just("capacity"));

        User user = User.builder().id(1L).email("test@example.com").role(co.com.bancolombia.model.Role.builder().name("ADMIN").build()).baseSalary(1000.0).build();
        Mono<String> result = adapter.calculateCapacity(new Object(), user, String.class);
        assert result.block().equals("capacity");
    }


    @Test
    void getSolicitudes_shouldReturnMono() {
        WebClient.RequestHeadersUriSpec uriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
            when(webClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(any(String.class))).thenReturn(headersSpec);
            when(headersSpec.exchangeToMono(any())).thenReturn(Mono.just("solicitudes"));

        Mono<String> result = adapter.getSolicitudes(String.class, 1, 10);
        assert result.block().equals("solicitudes");
    }



    @Test
    void updateSolicitud_shouldReturnMono() {
        WebClient.RequestBodyUriSpec uriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = Mockito.mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec headersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
            when(webClient.put()).thenReturn(uriSpec);
            when(uriSpec.uri(any(String.class))).thenReturn(bodySpec);
            when(bodySpec.header(any(), any())).thenReturn(bodySpec);
            when(bodySpec.bodyValue(any())).thenReturn(headersSpec);
            when(headersSpec.exchangeToMono(any())).thenReturn(Mono.just("updated"));

        Mono<String> result = adapter.updateSolicitud(new Object(), String.class);
        assert result.block().equals("updated");
    }
}
