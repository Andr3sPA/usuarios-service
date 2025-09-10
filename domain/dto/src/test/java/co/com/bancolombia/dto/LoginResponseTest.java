package co.com.bancolombia.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginResponseTest {

    @Test
    void testLoginResponseBuilderAndFields() {
        LoginResponse response = LoginResponse.builder()
                .message("Login successful")
                .userEmail("test@example.com")
                .role("USER")
                .build();

        assertEquals("Login successful", response.getMessage());
        assertEquals("test@example.com", response.getUserEmail());
        assertEquals("USER", response.getRole());
    }
}
