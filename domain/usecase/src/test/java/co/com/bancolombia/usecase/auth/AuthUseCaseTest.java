package co.com.bancolombia.usecase.auth;

import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.AuthenticationGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class AuthUseCaseTest {

    @Mock
    private AuthenticationGateway authGateway;

    @InjectMocks
    private AuthUseCase authUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        when(authGateway.authenticateUser("test@example.com", "password"))
                .thenReturn(Mono.just("jwt-token"));

        StepVerifier.create(authUseCase.login("test@example.com", "password"))
                .expectNext("jwt-token")
                .verifyComplete();
    }

    @Test
    void testLoginFailure() {
        when(authGateway.authenticateUser("test@example.com", "wrongpassword"))
                .thenReturn(Mono.error(new RuntimeException("Invalid credentials")));

        StepVerifier.create(authUseCase.login("test@example.com", "wrongpassword"))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void testValidateTokenAndGetUser() {
        User user = User.builder().id(1L).email("test@example.com").build();

        when(authGateway.validateTokenAndGetUser("valid-token"))
                .thenReturn(Mono.just(user));

        StepVerifier.create(authUseCase.validateTokenAndGetUser("valid-token"))
                .expectNext(user)
                .verifyComplete();
    }
}
