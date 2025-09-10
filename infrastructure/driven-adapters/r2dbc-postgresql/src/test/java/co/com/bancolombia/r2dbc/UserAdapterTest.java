package co.com.bancolombia.r2dbc;

import co.com.bancolombia.exception.EmailAlreadyExistsException;
import co.com.bancolombia.model.Role;
import co.com.bancolombia.model.User;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import co.com.bancolombia.r2dbc.mapper.RoleMapper;
import co.com.bancolombia.r2dbc.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserAdapterTest {

    @Mock
    private ReactiveUserRepository userRepo;

    @Mock
    private ReactiveRoleRepository roleRepo;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAdapter userAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_whenEmailExists_shouldReturnError() {
        User user = User.builder().email("test@example.com").build();

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Mono.just(new UserEntity()));

        StepVerifier.create(userAdapter.register(user))
                .expectError(EmailAlreadyExistsException.class)
                .verify();

        verify(userRepo).findByEmail(user.getEmail());
        verifyNoMoreInteractions(userRepo, roleRepo, userMapper, roleMapper);
    }

    @Test
    void register_whenEmailNotExists_shouldSaveUser() {
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.builder().id(1L).build())
                .build();

        String encodedPassword = "encodedPassword";
        User expectedUser = User.builder()
                .email("test@example.com")
                .password(encodedPassword)
                .role(Role.builder().id(1L).build())
                .build();

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());

        when(passwordEncoder.encode("password")).thenReturn(encodedPassword);
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(roleRepo.findById(user.getRole().getId())).thenReturn(Mono.just(new co.com.bancolombia.r2dbc.entity.RoleEntity()));
        when(userMapper.toEntity(any(User.class))).thenReturn(userEntity);
        when(userRepo.save(userEntity)).thenReturn(Mono.just(userEntity));
        when(userMapper.toModel(userEntity)).thenReturn(expectedUser);

        StepVerifier.create(userAdapter.register(user))
                .expectNext(expectedUser)
                .verifyComplete();

        verify(passwordEncoder).encode("password");
        verify(userRepo).findByEmail(user.getEmail());
        verify(roleRepo).findById(user.getRole().getId());
        verify(userMapper).toEntity(any(User.class));
        verify(userRepo).save(userEntity);
        verify(userMapper).toModel(userEntity);
    }

    @Test
    void findByEmail_whenUserExists_shouldReturnUser() {
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setRoleId(1L);

        co.com.bancolombia.r2dbc.entity.RoleEntity roleEntity = new co.com.bancolombia.r2dbc.entity.RoleEntity();

        User user = User.builder().email(email).role(Role.builder().id(1L).build()).build();

        when(userRepo.findByEmail(email)).thenReturn(Mono.just(userEntity));
        when(roleRepo.findById(userEntity.getRoleId())).thenReturn(Mono.just(roleEntity));
        when(userMapper.toModel(userEntity)).thenReturn(user);
        when(roleMapper.toModel(roleEntity)).thenReturn(user.getRole());

        StepVerifier.create(userAdapter.findByEmail(email))
                .expectNext(user)
                .verifyComplete();

        verify(userRepo).findByEmail(email);
        verify(roleRepo).findById(userEntity.getRoleId());
        verify(userMapper).toModel(userEntity);
        verify(roleMapper).toModel(roleEntity);
    }

    @Test
    void findByEmail_whenUserNotFound_shouldCompleteEmpty() {
        String email = "notfound@example.com";

        when(userRepo.findByEmail(email)).thenReturn(Mono.empty());

        StepVerifier.create(userAdapter.findByEmail(email))
                .expectComplete()
                .verify();

        verify(userRepo).findByEmail(email);
        verifyNoMoreInteractions(roleRepo, userMapper, roleMapper);
    }
}
