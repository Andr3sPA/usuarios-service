package co.com.bancolombia.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testUserBuilderAndFields() {
        User user = User.builder()
                .id(1L)
                .firstName("Andres")
                .lastName("Pena")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Calle 123")
                .phone("123456789")
                .email("test@example.com")
                .baseSalary(1000.0)
                .build();
        assertEquals(1L, user.getId());
        assertEquals("Andres", user.getFirstName());
        assertEquals("Pena", user.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthDate());
        assertEquals("Calle 123", user.getAddress());
        assertEquals("123456789", user.getPhone());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(1000.0, user.getBaseSalary());
    }
}
