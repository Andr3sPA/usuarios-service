package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.util.AuthenticationUtil;
import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.dto.LoginRequest;
import co.com.bancolombia.dto.LoginResponse;
import co.com.bancolombia.dto.SessionResponse;
import co.com.bancolombia.model.User;
import co.com.bancolombia.usecase.auth.AuthUseCase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HandlerAuthTest {

    @Mock
    private AuthUseCase authUseCase;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private AuthenticationUtil authenticationUtil;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private HandlerAuth handlerAuth;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        when(serverRequest.bodyToMono(LoginRequest.class)).thenReturn(Mono.just(loginRequest));
        when(authUseCase.login("test@example.com", "password")).thenReturn(Mono.just("jwt-token"));

        Mono<ServerResponse> responseMono = handlerAuth.login(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> {
                    // Verificar que la respuesta es OK y contiene el cookie
                    return serverResponse.statusCode().is2xxSuccessful() &&
                           serverResponse.headers().getContentType().includes(MediaType.APPLICATION_JSON);
                })
                .verifyComplete();
    }

    @Test
    void testLoginFailure() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("wrongpassword")
                .build();

        when(serverRequest.bodyToMono(LoginRequest.class)).thenReturn(Mono.just(loginRequest));
        when(authUseCase.login("test@example.com", "wrongpassword")).thenReturn(Mono.error(new RuntimeException("Invalid credentials")));

        Mono<ServerResponse> responseMono = handlerAuth.login(serverRequest);

        StepVerifier.create(responseMono)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void testLogoutSuccess() {
        SessionResponse sessionResponse = SessionResponse.builder().email("test@example.com").build();

        when(authenticationUtil.getAuthenticatedUser(serverRequest)).thenReturn(Mono.just(sessionResponse));

        Mono<ServerResponse> responseMono = handlerAuth.logout(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }
}
