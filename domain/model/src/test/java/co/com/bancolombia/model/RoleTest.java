package co.com.bancolombia.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    void testRoleBuilderAndFields() {
        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .description("Administrator role")
                .build();

        assertEquals(1L, role.getId());
        assertEquals("ADMIN", role.getName());
        assertEquals("Administrator role", role.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        Role role1 = Role.builder()
                .id(1L)
                .name("ADMIN")
                .description("Administrator role")
                .build();

        Role role2 = Role.builder()
                .id(1L)
                .name("ADMIN")
                .description("Administrator role")
                .build();

        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());
    }
}
