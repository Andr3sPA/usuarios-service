package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.dto.UserRegisterRequest;
import co.com.bancolombia.model.Role;
import co.com.bancolombia.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {

    @Mapping(source = "nombres", target = "firstName")
    @Mapping(source = "apellidos", target = "lastName")
    @Mapping(source = "fechaNacimiento", target = "birthDate")
    @Mapping(source = "direccion", target = "address")
    @Mapping(source = "telefono", target = "phone")
    @Mapping(source = "correoElectronico", target = "email")
    @Mapping(source = "salarioBase", target = "baseSalary")
    @Mapping(target = "role.id", source = "roleId")
    User toModel(UserRegisterRequest dto);

}
