package co.com.bancolombia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String address;
    private String phone;
    private String email;
    private Double baseSalary;
    private RoleResponse role;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleResponse {
        private Long id;
        private String name;
        private String description;
    }
}
