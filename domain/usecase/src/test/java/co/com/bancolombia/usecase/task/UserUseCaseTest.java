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
import static org.mockito.Mockito.*;

class UserUseCaseTest {
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        User user = new User();
        when(repository.register(user)).thenReturn(Mono.just(user));
        Mono<User> result = useCase.register(user);
        assertNotNull(result.block());
        verify(repository).register(user);
    }
}
