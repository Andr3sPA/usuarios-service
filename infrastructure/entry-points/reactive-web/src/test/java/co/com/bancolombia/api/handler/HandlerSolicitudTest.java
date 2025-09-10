package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.util.AuthenticationUtil;
import co.com.bancolombia.dto.SessionResponse;
import co.com.bancolombia.model.User;
import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import com.fasterxml.jackson.databind.JsonNode;
import org.reactivecommons.utils.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class HandlerSolicitudTest {

    @Mock
    private SolicitudUseCase solicitudUseCase;

    @Mock
    private AuthenticationUtil authenticationUtil;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private HandlerSolicitud handlerSolicitud;

    private SessionResponse sessionResponse;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sessionResponse = SessionResponse.builder().id(1L).email("test@example.com").build();
        user = User.builder().id(1L).email("test@example.com").build();
    }

    @Test
    void testCreateSolicitudSuccess() {
        JsonNode solicitudData = new com.fasterxml.jackson.databind.ObjectMapper().createObjectNode().put("type", "loan");

        when(authenticationUtil.getAuthenticatedUser(serverRequest)).thenReturn(Mono.just(sessionResponse));
        when(serverRequest.bodyToMono(JsonNode.class)).thenReturn(Mono.just(solicitudData));
        when(mapper.map(any(SessionResponse.class), eq(User.class))).thenReturn(user);
        when(solicitudUseCase.createSolicitud(any(JsonNode.class), any(User.class), any(Class.class))).thenReturn(Mono.just(solicitudData));

        Mono<ServerResponse> responseMono = handlerSolicitud.createSolicitud(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void testGetSolicitudesSuccess() {
        JsonNode result = new com.fasterxml.jackson.databind.ObjectMapper().createObjectNode().put("data", "solicitudes");

        when(authenticationUtil.getAuthenticatedUser(serverRequest)).thenReturn(Mono.just(sessionResponse));
        when(serverRequest.queryParam("page")).thenReturn(Optional.of("0"));
        when(serverRequest.queryParam("size")).thenReturn(Optional.of("10"));
        when(solicitudUseCase.getSolicitudes(any(Class.class), anyInt(), anyInt())).thenReturn(Mono.just(result));

        Mono<ServerResponse> responseMono = handlerSolicitud.getSolicitudes(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void testUpdateSolicitudSuccess() {
        JsonNode solicitudData = new com.fasterxml.jackson.databind.ObjectMapper().createObjectNode().put("status", "approved");

        when(authenticationUtil.getAuthenticatedUser(serverRequest)).thenReturn(Mono.just(sessionResponse));
        when(serverRequest.bodyToMono(JsonNode.class)).thenReturn(Mono.just(solicitudData));
        when(solicitudUseCase.updateSolicitud(any(JsonNode.class), any(Class.class))).thenReturn(Mono.just(solicitudData));

        Mono<ServerResponse> responseMono = handlerSolicitud.updateSolicitud(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }
}
