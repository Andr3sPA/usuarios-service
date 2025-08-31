package co.com.bancolombia.api.handler;

import co.com.bancolombia.model.User;
import co.com.bancolombia.usecase.user.UserUseCase;
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
    @InjectMocks
    private HandlerUser handlerUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        User user = new User();
        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.bodyToMono(User.class)).thenReturn(Mono.just(user));
        when(userUseCase.register(user)).thenReturn(Mono.just(user));

        Mono<ServerResponse> responseMono = handlerUser.registerUser(serverRequest);
        ServerResponse response = responseMono.block();
        assertNotNull(response);
        assertEquals(MediaType.APPLICATION_JSON, response.headers().getContentType());
        verify(serverRequest).bodyToMono(User.class);
        verify(userUseCase).register(user);
    }
}
