package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.util.AuthenticationUtil;
import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.dto.LoginRequest;
import co.com.bancolombia.usecase.auth.AuthUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HandlerAuthAdditionalTest {

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
    void testLoginWithInvalidRequest() {
        when(serverRequest.bodyToMono(LoginRequest.class)).thenReturn(Mono.error(new RuntimeException("Invalid request")));

        Mono<ServerResponse> responseMono = handlerAuth.login(serverRequest);

        StepVerifier.create(responseMono)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void testLogoutWhenUserNotAuthenticated() {
        when(authenticationUtil.getAuthenticatedUser(serverRequest)).thenReturn(Mono.error(new RuntimeException("User not authenticated")));

        Mono<ServerResponse> responseMono = handlerAuth.logout(serverRequest);

        StepVerifier.create(responseMono)
                .expectError(RuntimeException.class)
                .verify();
    }
}
