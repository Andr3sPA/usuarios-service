package co.com.bancolombia.dto;

import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRegisterRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testValidUserRegisterRequest() {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main Street")
                .phone("1234567890")
                .email("john.doe@example.com")
                .roleId(1L)
                .baseSalary(5000.0)
                .password("SecurePass123!")
                .build();

        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmail() {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main Street")
                .phone("1234567890")
                .email("invalid-email")
                .roleId(1L)
                .baseSalary(5000.0)
                .password("SecurePass123!")
                .build();

        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Invalid email format")));
    }

    @Test
    void testInvalidPassword() {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main Street")
                .phone("1234567890")
                .email("john.doe@example.com")
                .roleId(1L)
                .baseSalary(5000.0)
                .password("weak")
                .build();

        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Must include uppercase")));
    }

    @Test
    void testBuilderPattern() {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .build();

        assertEquals("Jane", request.getFirstName());
        assertEquals("Smith", request.getLastName());
        assertNull(request.getEmail());
    }

    @Test
    void testEqualsAndHashCode() {
        UserRegisterRequest request1 = UserRegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        UserRegisterRequest request2 = UserRegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}