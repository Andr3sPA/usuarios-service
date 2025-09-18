package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.Role;
import co.com.bancolombia.r2dbc.entity.RoleEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleMapperImplTest {
    private final RoleMapperImpl mapper = new RoleMapperImpl();

    @Test
    void toModel_shouldMapEntityToRole() {
        RoleEntity entity = new RoleEntity();
        entity.setId(1L);
        entity.setName("ADMIN");
        entity.setDescription("Administrador");
        Role role = mapper.toModel(entity);
        assertEquals(1L, role.getId());
        assertEquals("ADMIN", role.getName());
        assertEquals("Administrador", role.getDescription());
    }

    @Test
    void toEntity_shouldMapRoleToEntity() {
        Role role = Role.builder().id(2L).name("USER").description("Usuario").build();
        RoleEntity entity = mapper.toEntity(role);
        assertEquals(2L, entity.getId());
        assertEquals("USER", entity.getName());
        assertEquals("Usuario", entity.getDescription());
    }
}
