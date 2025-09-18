package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.dto.UserRegisterRequest;
import co.com.bancolombia.model.Role;
import co.com.bancolombia.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestMapperImplTest {
    private final UserRequestMapperImpl mapper = new UserRequestMapperImpl();

    @Test
    void toModel_shouldMapRequestToUser() {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("test@example.com")
                .password("pass")
                .roleId(1L)
                .build();
        User user = mapper.toModel(request);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("pass", user.getPassword());
        assertEquals(1L, user.getRole().getId());
    }

    @Test
    void userRegisterRequestToRole_shouldMapRequestToRole() {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .roleId(2L)
                .build();
        Role role = mapper.userRegisterRequestToRole(request);
        assertEquals(2L, role.getId());
    }
}
