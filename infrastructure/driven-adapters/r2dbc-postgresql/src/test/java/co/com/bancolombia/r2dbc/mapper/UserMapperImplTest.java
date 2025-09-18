package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.Role;
import co.com.bancolombia.model.User;
import co.com.bancolombia.r2dbc.entity.RoleEntity;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperImplTest {
    private final UserMapperImpl mapper = new UserMapperImpl();

    @Test
    void toEntity_shouldMapUserToEntity() {
        Role role = Role.builder().id(1L).name("ADMIN").description("Administrador").build();
        User user = User.builder().id(10L).email("test@example.com").role(role).build();
        UserEntity entity = mapper.toEntity(user);
        assertEquals(10L, entity.getId());
        assertEquals("test@example.com", entity.getEmail());
        assertEquals(1L, entity.getRoleId());
    }

    @Test
    void toModel_shouldMapEntityToUser() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(2L);
        roleEntity.setName("USER");
        roleEntity.setDescription("Usuario");
        UserEntity entity = new UserEntity();
        entity.setId(20L);
        entity.setEmail("user@example.com");
        entity.setRoleId(2L);
        User user = mapper.toModel(entity);
        assertEquals(20L, user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals(2L, user.getRole().getId());
    }
}
