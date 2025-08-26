package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.User;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryAdapterTest {
    @Mock
    private ReactiveUserRepository repo;
    @Mock
    private ObjectMapper mapper;
    @InjectMocks
    private UserRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        User user = new User();
        UserEntity entity = new UserEntity();
        when(mapper.map(user, UserEntity.class)).thenReturn(entity);
        when(repo.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        Mono<User> result = adapter.register(user);
        User userResult = result.block();
        assertNotNull(userResult);
        verify(repo).save(entity);
        verify(mapper, times(1)).map(user, UserEntity.class);
        verify(mapper, times(1)).map(entity, User.class);
    }

    @Test
    void testFindByEmail() {
        String email = "test@example.com";
        UserEntity entity = new UserEntity();
        User user = new User();
        when(repo.findByEmail(email)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        Mono<User> result = adapter.findByEmail(email);
        User userResult = result.block();
        assertNotNull(userResult);
        verify(repo).findByEmail(email);
        verify(mapper).map(entity, User.class);
    }
}
