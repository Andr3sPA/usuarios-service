package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.JwtPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenAdapterTest {

    private JwtTokenAdapter jwtTokenAdapter;
    private final String secret = "testSecretKeyForTestingPurposesOnly";
    private final long expirationTimeMillis = 86400000L; // 1 day

    @BeforeEach
    void setUp() {
        jwtTokenAdapter = new JwtTokenAdapter(secret, expirationTimeMillis);
    }

    @Test
    void testGenerateToken() {
        // Given
        Instant now = Instant.now();
        JwtPayload payload = JwtPayload.builder()
                .subject("testUser")
                .userId(123L)
                .role("USER")
                .issuedAt(now)
                .expiresAt(now.plusMillis(expirationTimeMillis))
                .build();

        // When & Then
        StepVerifier.create(jwtTokenAdapter.generateToken(payload))
                .expectNextMatches(token -> {
                    assertNotNull(token);
                    assertFalse(token.isEmpty());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testValidateToken() {
        // Given
        Instant now = Instant.now();
        JwtPayload originalPayload = JwtPayload.builder()
                .subject("testUser")
                .userId(123L)
                .role("USER")
                .issuedAt(now)
                .expiresAt(now.plusMillis(expirationTimeMillis))
                .build();

        // When
        String token = jwtTokenAdapter.generateToken(originalPayload).block();

        // Then
        StepVerifier.create(jwtTokenAdapter.validateToken(token))
                .expectNextMatches(validatedPayload -> {
                    assertEquals(originalPayload.getSubject(), validatedPayload.getSubject());
                    assertEquals(originalPayload.getUserId(), validatedPayload.getUserId());
                    assertEquals(originalPayload.getRole(), validatedPayload.getRole());
                    assertNotNull(validatedPayload.getIssuedAt());
                    assertNotNull(validatedPayload.getExpiresAt());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testValidateTokenWithInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When & Then
        StepVerifier.create(jwtTokenAdapter.validateToken(invalidToken))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void testIsTokenExpired() {
        // Given
        Instant now = Instant.now();
        JwtPayload payload = JwtPayload.builder()
                .subject("testUser")
                .userId(123L)
                .role("USER")
                .issuedAt(now.minusMillis(expirationTimeMillis * 2)) // Expired 2 days ago
                .expiresAt(now.minusMillis(expirationTimeMillis))
                .build();

        // When
        String token = jwtTokenAdapter.generateToken(payload).block();

        // Then
        StepVerifier.create(jwtTokenAdapter.isTokenExpired(token))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testIsTokenNotExpired() {
        // Given
        Instant now = Instant.now();
        JwtPayload payload = JwtPayload.builder()
                .subject("testUser")
                .userId(123L)
                .role("USER")
                .issuedAt(now)
                .expiresAt(now.plusMillis(expirationTimeMillis))
                .build();

        // When
        String token = jwtTokenAdapter.generateToken(payload).block();

        // Then
        StepVerifier.create(jwtTokenAdapter.isTokenExpired(token))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void testIsTokenExpiredWithInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When & Then
        StepVerifier.create(jwtTokenAdapter.isTokenExpired(invalidToken))
                .expectNext(true)
                .verifyComplete();
    }
}
