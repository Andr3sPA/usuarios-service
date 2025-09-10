package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.dto.UserRegisterRequest;
import co.com.bancolombia.model.User;
import co.com.bancolombia.usecase.user.UserUseCase;
import co.com.bancolombia.r2dbc.mapper.UserRequestMapper;
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

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HandlerUserTest {

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private UserRequestMapper userRequestMapper;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private HandlerUser handlerUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserSuccess() {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();

        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        when(serverRequest.bodyToMono(UserRegisterRequest.class)).thenReturn(Mono.just(request));
        when(userRequestMapper.toModel(request)).thenReturn(user);
        when(userUseCase.register(user)).thenReturn(Mono.just(user));

        Mono<ServerResponse> responseMono = handlerUser.registerUser(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> {
                    return serverResponse.statusCode().is2xxSuccessful() &&
                           serverResponse.headers().getContentType().includes(MediaType.APPLICATION_JSON);
                })
                .verifyComplete();
    }

    @Test
    void testRegisterUserFailure() {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();

        when(serverRequest.bodyToMono(UserRegisterRequest.class)).thenReturn(Mono.just(request));
        when(userUseCase.register(any(User.class))).thenReturn(Mono.error(new RuntimeException("Registration failed")));

        Mono<ServerResponse> responseMono = handlerUser.registerUser(serverRequest);

        StepVerifier.create(responseMono)
                .expectError(RuntimeException.class)
                .verify();
    }
}
