package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.dto.UserRegisterRequest;
import co.com.bancolombia.model.Role;
import co.com.bancolombia.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface UserRequestMapper {

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "baseSalary", target = "baseSalary")
    @Mapping(target = "role.id", source = "roleId")
    @Mapping(target = "id", ignore = true) // Agregar esta línea
    User toModel(UserRegisterRequest dto);
}