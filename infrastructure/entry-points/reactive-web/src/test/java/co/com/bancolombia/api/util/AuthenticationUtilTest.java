package co.com.bancolombia.api.util;

import org.junit.jupiter.api.Test;



import org.reactivecommons.utils.ObjectMapper;
import org.mockito.Mockito;

class AuthenticationUtilTest {
    @Test
    void testConstructor() {
        ObjectMapper mapper = Mockito.mock(ObjectMapper.class);
        AuthenticationUtil util = new AuthenticationUtil(mapper);
        assert util != null;
    }

    @Test
    void testGetAuthenticatedUserSuccess() {
        ObjectMapper mapper = Mockito.mock(ObjectMapper.class);
        AuthenticationUtil util = new AuthenticationUtil(mapper);
        org.springframework.web.reactive.function.server.ServerRequest request = Mockito.mock(org.springframework.web.reactive.function.server.ServerRequest.class);
        co.com.bancolombia.model.User user = Mockito.mock(co.com.bancolombia.model.User.class);
        co.com.bancolombia.dto.SessionResponse sessionResponse = Mockito.mock(co.com.bancolombia.dto.SessionResponse.class);
        Mockito.when(request.attribute("authenticatedUser")).thenReturn(java.util.Optional.of(user));
        Mockito.when(mapper.map(user, co.com.bancolombia.dto.SessionResponse.class)).thenReturn(sessionResponse);
        var result = util.getAuthenticatedUser(request).block();
        assert result == sessionResponse;
    }

    @Test
    void testGetAuthenticatedUserThrowsException() {
        ObjectMapper mapper = Mockito.mock(ObjectMapper.class);
        AuthenticationUtil util = new AuthenticationUtil(mapper);
        org.springframework.web.reactive.function.server.ServerRequest request = Mockito.mock(org.springframework.web.reactive.function.server.ServerRequest.class);
        Mockito.when(request.attribute("authenticatedUser")).thenReturn(java.util.Optional.empty());
        try {
            util.getAuthenticatedUser(request).block();
            assert false;
        } catch (co.com.bancolombia.exception.UserNotAuthenticatedException e) {
            assert true;
        }
    }
}
