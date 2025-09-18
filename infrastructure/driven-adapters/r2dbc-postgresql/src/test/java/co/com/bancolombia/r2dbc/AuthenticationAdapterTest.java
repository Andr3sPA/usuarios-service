package co.com.bancolombia.r2dbc;

import co.com.bancolombia.exception.InvalidCredentialsException;
import co.com.bancolombia.exception.InvalidTokenException;
import co.com.bancolombia.model.JwtPayload;
import co.com.bancolombia.model.Role;
import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.JwtTokenGateway;
import co.com.bancolombia.model.gateways.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationAdapterTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private JwtTokenGateway jwtTokenGateway;

    @InjectMocks
    private AuthenticationAdapter authenticationAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationAdapter = new AuthenticationAdapter(userGateway, jwtTokenGateway);
    }

    @Test
    void authenticateUser_whenUserExistsAndPasswordMatches_shouldReturnToken() {
        String email = "test@example.com";
        String password = "password";
        // Generar un password codificado real con BCrypt
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        String token = "jwt.token.here";

        User user = User.builder()
                .id(1L)
                .email(email)
                .password(encodedPassword)
                .role(Role.builder().name("USER").build())
                .build();

        when(userGateway.findByEmail(email)).thenReturn(Mono.just(user));
        when(jwtTokenGateway.generateToken(any(JwtPayload.class))).thenReturn(Mono.just(token));

        StepVerifier.create(authenticationAdapter.authenticateUser(email, password))
                .expectNext(token)
                .verifyComplete();

        verify(userGateway).findByEmail(email);
        verify(jwtTokenGateway).generateToken(any(JwtPayload.class));
    }

    @Test
    void authenticateUser_whenUserNotFound_shouldReturnInvalidCredentialsException() {
        String email = "notfound@example.com";
        String password = "password";

        when(userGateway.findByEmail(email)).thenReturn(Mono.empty());

        StepVerifier.create(authenticationAdapter.authenticateUser(email, password))
                .expectError(InvalidCredentialsException.class)
                .verify();

        verify(userGateway).findByEmail(email);
        verifyNoInteractions(jwtTokenGateway);
    }

    @Test
    void authenticateUser_whenPasswordDoesNotMatch_shouldReturnInvalidCredentialsException() {
        String email = "test@example.com";
        String password = "wrongpassword";
        String encodedPassword = "$2a$10$encodedPassword"; // BCrypt encoded

        User user = User.builder()
                .id(1L)
                .email(email)
                .password(encodedPassword)
                .role(Role.builder().name("USER").build())
                .build();

        when(userGateway.findByEmail(email)).thenReturn(Mono.just(user));

        StepVerifier.create(authenticationAdapter.authenticateUser(email, password))
                .expectError(InvalidCredentialsException.class)
                .verify();

        verify(userGateway).findByEmail(email);
        verifyNoInteractions(jwtTokenGateway);
    }

    @Test
    void validateTokenAndGetUser_whenTokenValid_shouldReturnUser() {
        String token = "valid.jwt.token";
        String email = "test@example.com";

        JwtPayload payload = JwtPayload.builder()
                .subject(email)
                .userId(1L)
                .role("USER")
                .build();

        User user = User.builder()
                .id(1L)
                .email(email)
                .role(Role.builder().name("USER").build())
                .build();

        when(jwtTokenGateway.validateToken(token)).thenReturn(Mono.just(payload));
        when(userGateway.findByEmail(email)).thenReturn(Mono.just(user));

        StepVerifier.create(authenticationAdapter.validateTokenAndGetUser(token))
                .expectNext(user)
                .verifyComplete();

        verify(jwtTokenGateway).validateToken(token);
        verify(userGateway).findByEmail(email);
    }

    @Test
    void validateTokenAndGetUser_whenTokenInvalid_shouldReturnInvalidTokenException() {
        String token = "invalid.jwt.token";

        when(jwtTokenGateway.validateToken(token)).thenReturn(Mono.error(new RuntimeException("Invalid token")));

        StepVerifier.create(authenticationAdapter.validateTokenAndGetUser(token))
                .expectError(InvalidTokenException.class)
                .verify();

        verify(jwtTokenGateway).validateToken(token);
        verifyNoInteractions(userGateway);
    }
}
