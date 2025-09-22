package co.com.bancolombia.api.filter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


import co.com.bancolombia.api.config.LoanAppPath;
import co.com.bancolombia.api.config.UserPath;
import co.com.bancolombia.usecase.auth.AuthUseCase;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.http.HttpCookie;
import org.springframework.util.MultiValueMap;

class JwtAuthenticationFilterTest {
    @Test
    void testFilterCallsNext() {
        LoanAppPath loanAppPath = Mockito.mock(LoanAppPath.class);
        AuthUseCase authUseCase = Mockito.mock(AuthUseCase.class);
        UserPath userPath = Mockito.mock(UserPath.class);
    Mockito.when(userPath.getLogin()).thenReturn("/api/v1/login");
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(loanAppPath, authUseCase, userPath);
    ServerRequest request = Mockito.mock(ServerRequest.class);
    HandlerFunction<ServerResponse> next = Mockito.mock(HandlerFunction.class);
    ServerResponse response = Mockito.mock(ServerResponse.class);
    // Mock de path y método
    Mockito.when(request.path()).thenReturn("/api/v1/usuarios");
    Mockito.when(request.methodName()).thenReturn("GET");
        // Mock de cookies
        HttpCookie cookie = Mockito.mock(HttpCookie.class);
        Mockito.when(cookie.getValue()).thenReturn("token");
        MultiValueMap<String, HttpCookie> cookies = Mockito.mock(MultiValueMap.class);
        Mockito.when(request.cookies()).thenReturn(cookies);
        Mockito.when(cookies.getFirst("jwt-token")).thenReturn(cookie);
        // Mock de AuthUseCase
        co.com.bancolombia.model.User user = Mockito.mock(co.com.bancolombia.model.User.class);
        co.com.bancolombia.model.Role role = Mockito.mock(co.com.bancolombia.model.Role.class);
        Mockito.when(user.getEmail()).thenReturn("test@correo.com");
        Mockito.when(user.getRole()).thenReturn(role);
        Mockito.when(role.getName()).thenReturn("ADMIN");
        Mockito.when(authUseCase.validateTokenAndGetUser("token")).thenReturn(Mono.just(user));
        Mockito.when(next.handle(Mockito.any())).thenReturn(Mono.just(response));
        Mono<ServerResponse> result = filter.filter(request, next);
        // Verifica que el Mono se complete correctamente
        ServerResponse serverResponse = result.block();
        assert serverResponse != null;
    }
}
