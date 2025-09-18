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
        Role expectedRole = Role.builder().id(1L).name("ROLE_USER").description("Usuario").build();
        User expectedUser = User.builder()
                .id(null)
                .firstName(null)
                .lastName(null)
                .birthDate(null)
                .address(null)
                .phone(null)
                .email("test@example.com")
                .password(encodedPassword)
                .baseSalary(null)
                .role(expectedRole)
                .build();

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());

        co.com.bancolombia.r2dbc.entity.RoleEntity roleEntity = new co.com.bancolombia.r2dbc.entity.RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("ROLE_USER");
        roleEntity.setDescription("Usuario");

        when(passwordEncoder.encode("password")).thenReturn(encodedPassword);
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(roleRepo.findById(user.getRole().getId())).thenReturn(Mono.just(roleEntity));
        when(roleMapper.toModel(roleEntity)).thenReturn(expectedRole);
        when(userMapper.toEntity(any(User.class))).thenReturn(userEntity);
        when(userRepo.save(userEntity)).thenReturn(Mono.just(userEntity));
        doReturn(expectedUser).when(userMapper).toModel(userEntity);

        StepVerifier.create(userAdapter.register(user))
                .assertNext(actualUser -> {
                    assertEquals(expectedUser.getEmail(), actualUser.getEmail());
                    assertEquals(expectedUser.getPassword(), actualUser.getPassword());
                    assertEquals(expectedUser.getRole().getId(), actualUser.getRole().getId());
                    assertEquals(expectedUser.getRole().getName(), actualUser.getRole().getName());
                    assertEquals(expectedUser.getRole().getDescription(), actualUser.getRole().getDescription());
                    assertEquals(expectedUser.getId(), actualUser.getId());
                    assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
                    assertEquals(expectedUser.getLastName(), actualUser.getLastName());
                    assertEquals(expectedUser.getBirthDate(), actualUser.getBirthDate());
                    assertEquals(expectedUser.getAddress(), actualUser.getAddress());
                    assertEquals(expectedUser.getPhone(), actualUser.getPhone());
                    assertEquals(expectedUser.getBaseSalary(), actualUser.getBaseSalary());
                })
                .verifyComplete();

        verify(passwordEncoder).encode("password");
        verify(userRepo).findByEmail(user.getEmail());
        verify(roleRepo).findById(user.getRole().getId());
        verify(roleMapper).toModel(roleEntity);
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
