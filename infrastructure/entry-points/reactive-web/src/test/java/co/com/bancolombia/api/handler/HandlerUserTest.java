package co.com.bancolombia.api.handler;

import co.com.bancolombia.dto.UserRegisterRequest;
import co.com.bancolombia.model.User;
import co.com.bancolombia.usecase.user.UserUseCase;
import co.com.bancolombia.r2dbc.mapper.UserRequestMapper;
import co.com.bancolombia.api.util.RequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HandlerUserTest {
    @Mock
    private UserUseCase userUseCase;
    @Mock
    private UserRequestMapper userRequestMapper;
    @Mock
    private RequestValidator requestValidator;
    @InjectMocks
    private HandlerUser handlerUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        UserRegisterRequest dto = new UserRegisterRequest();
        User user = new User();
        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.bodyToMono(eq(UserRegisterRequest.class))).thenReturn(Mono.just(dto));
        doNothing().when(requestValidator).validate(dto, UserRegisterRequest.class);
        when(userRequestMapper.toModel(dto)).thenReturn(user);
        when(userUseCase.register(user)).thenReturn(Mono.just(user));

        Mono<ServerResponse> responseMono = handlerUser.registerUser(serverRequest);
        ServerResponse response = responseMono.block();
        assertNotNull(response);
        assertEquals(MediaType.APPLICATION_JSON, response.headers().getContentType());
        verify(serverRequest).bodyToMono(UserRegisterRequest.class);
        verify(requestValidator).validate(dto, UserRegisterRequest.class);
        verify(userRequestMapper).toModel(dto);
        verify(userUseCase).register(user);
    }
}
