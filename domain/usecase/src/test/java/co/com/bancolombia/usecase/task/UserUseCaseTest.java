package co.com.bancolombia.usecase.task;

import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.*;
package co.com.bancolombia.usecase.task;

import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.UserRepository;
import co.com.bancolombia.model.gateways.TransactionalGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserUseCaseTest {
    @Mock
    private UserRepository repository;
    @Mock
    private TransactionalGateway transactionalGateway;
    @InjectMocks
    private UserUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        User user = new User();
        when(repository.register(user)).thenReturn(Mono.just(user));
        when(transactionalGateway.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mono<User> result = useCase.register(user);
        assertNotNull(result.block());
        verify(repository).register(user);
        verify(transactionalGateway).transactional(any(Mono.class));
    }

    @Test
    void testRegisterRepositoryError() {
        User user = new User();
        when(repository.register(user)).thenReturn(Mono.error(new RuntimeException("fail")));
        when(transactionalGateway.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mono<User> result = useCase.register(user);
        assertThrows(RuntimeException.class, result::block);
        verify(repository).register(user);
        verify(transactionalGateway).transactional(any(Mono.class));
    }

    @Test
    void testRegisterTransactionalError() {
        User user = new User();
        when(repository.register(user)).thenReturn(Mono.just(user));
        when(transactionalGateway.transactional(any(Mono.class))).thenReturn(Mono.error(new RuntimeException("tx fail")));
        Mono<User> result = useCase.register(user);
        assertThrows(RuntimeException.class, result::block);
        verify(repository).register(user);
        verify(transactionalGateway).transactional(any(Mono.class));
    }
}
