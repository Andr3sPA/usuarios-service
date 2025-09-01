package co.com.bancolombia.usecase.task;

import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.UserGateway;
import co.com.bancolombia.model.gateways.TransactionalGateway;
import co.com.bancolombia.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class UserUseCaseTest {

    @Mock
    private UserGateway repository;

    @Mock
    private TransactionalGateway transactionalGateway;

    @InjectMocks
    private UserUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(repository.register(user)).thenReturn(Mono.just(user));
        when(transactionalGateway.transactional(any(Mono.class))).thenReturn(Mono.just(user));

        // Act & Assert
        StepVerifier.create(useCase.register(user))
                .expectNext(user)
                .verifyComplete();

        verify(transactionalGateway).transactional(any(Mono.class));
    }

    @Test
    void testRegisterWithError() {
        // Arrange
        User user = new User();
        RuntimeException exception = new RuntimeException("Database error");

        when(repository.register(user)).thenReturn(Mono.error(exception));
        when(transactionalGateway.transactional(any(Mono.class))).thenReturn(Mono.error(exception));

        // Act & Assert
        StepVerifier.create(useCase.register(user))
                .expectError(RuntimeException.class)
                .verify();

        verify(transactionalGateway).transactional(any(Mono.class));
    }
}