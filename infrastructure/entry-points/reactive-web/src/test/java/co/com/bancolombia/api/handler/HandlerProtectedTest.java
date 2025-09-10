package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.util.AuthenticationUtil;
import co.com.bancolombia.dto.SessionResponse;
import co.com.bancolombia.model.User;
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

import static org.mockito.Mockito.when;

class HandlerProtectedTest {

    @Mock
    private AuthenticationUtil authenticationUtil;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private HandlerProtected handlerProtected;

    private SessionResponse sessionResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sessionResponse = SessionResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
    }

    @Test
    void testGetProfileSuccess() {
        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        when(authenticationUtil.getAuthenticatedUser(serverRequest)).thenReturn(Mono.just(sessionResponse));

        Mono<ServerResponse> responseMono = handlerProtected.getProfile(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> {
                    return serverResponse.statusCode().is2xxSuccessful() &&
                           serverResponse.headers().getContentType().includes(MediaType.APPLICATION_JSON);
                })
                .verifyComplete();
    }

    @Test
    void testGetProfileFailure() {
        when(authenticationUtil.getAuthenticatedUser(serverRequest))
                .thenReturn(Mono.error(new RuntimeException("User not authenticated")));

        Mono<ServerResponse> responseMono = handlerProtected.getProfile(serverRequest);

        StepVerifier.create(responseMono)
                .expectError(RuntimeException.class)
                .verify();
    }
}
