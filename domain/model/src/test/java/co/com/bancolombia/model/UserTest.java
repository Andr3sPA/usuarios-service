package co.com.bancolombia.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest {
    @Test
    void testUserBuilderAndFields() {
        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .description("Administrator role")
                .build();
        User user = User.builder()
                .id(1L)
                .firstName("Andres")
                .lastName("Peña")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Calle 123")
                .phone("123456789")
                .email("test@example.com")
                .baseSalary(1000.0)
                .role(role)
                .password("password123")
                .build();
        assertEquals(1L, user.getId());
        assertEquals("Andres", user.getFirstName());
        assertEquals("Peña", user.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthDate());
        assertEquals("Calle 123", user.getAddress());
        assertEquals("123456789", user.getPhone());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(1000.0, user.getBaseSalary());
        assertEquals(role, user.getRole());
        assertEquals("password123", user.getPassword());
    }
}
