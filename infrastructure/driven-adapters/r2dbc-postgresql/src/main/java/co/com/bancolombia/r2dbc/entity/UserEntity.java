package co.com.bancolombia.r2dbc.entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    private Long id;

    @NotBlank
    @Column("first_name")
    private String firstName;

    @NotBlank
    @Column("last_name")
    private String lastName;

    @Column("birth_date")
    private LocalDate birthDate;

    @Column("address")
    private String address;

    @Column("phone")
    private String phone;

    @NotBlank
    @Email
    @Column("email")
    private String email;

    @NotBlank
    @Column("password")
    private String password;

    @NotNull
    @Min(0)
    @Max(15000000)
    @Column("base_salary")
    private Double baseSalary;

    @Column("role_id")
    private Long roleId;
}
