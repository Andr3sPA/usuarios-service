package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.User;
import co.com.bancolombia.model.Role;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import co.com.bancolombia.r2dbc.entity.RoleEntity;
import co.com.bancolombia.r2dbc.mapper.RoleMapper;
import co.com.bancolombia.r2dbc.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserGatewayAdapterTest {
    @Mock
    private ReactiveUserRepository userRepo;
    @Mock
    private ReactiveRoleRepository roleRepo;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RoleMapper roleMapper;

    private UserGatewayAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new UserGatewayAdapter(userRepo, roleRepo, userMapper, roleMapper);
    }

    @Test
    void testRegister() {
        User user = new User();
        Role role = new Role();
        role.setId(1L);
        user.setRole(role);
        user.setPassword("plainPassword");

        RoleEntity roleEntity = new RoleEntity();
        UserEntity userEntity = new UserEntity();

        when(roleRepo.findById(1L)).thenReturn(Mono.just(roleEntity));
        when(roleMapper.toModel(roleEntity)).thenReturn(role);
        when(userMapper.toEntity(any(User.class))).thenReturn(userEntity);
        when(userRepo.save(userEntity)).thenReturn(Mono.just(userEntity));
        when(userMapper.toModel(userEntity)).thenReturn(user);

        Mono<User> result = adapter.register(user);
        User userResult = result.block();
        assertNotNull(userResult);
        verify(roleRepo).findById(1L);
        verify(roleMapper).toModel(roleEntity);
        verify(userMapper).toEntity(any(User.class));
        verify(userRepo).save(userEntity);
        verify(userMapper).toModel(userEntity);
    }

    @Test
    void testFindByEmail() {
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity();
    userEntity.setRoleId(1L);
        RoleEntity roleEntity = new RoleEntity();
        User user = new User();
        Role role = new Role();

        when(userRepo.findByEmail(email)).thenReturn(Mono.just(userEntity));
    when(roleRepo.findById(1L)).thenReturn(Mono.just(roleEntity));
        when(userMapper.toModel(userEntity)).thenReturn(user);
        when(roleMapper.toModel(roleEntity)).thenReturn(role);

        Mono<User> result = adapter.findByEmail(email);
        User userResult = result.block();
        assertNotNull(userResult);
        verify(userRepo).findByEmail(email);
    verify(roleRepo).findById(1L);
        verify(userMapper).toModel(userEntity);
        verify(roleMapper).toModel(roleEntity);
    }
}
