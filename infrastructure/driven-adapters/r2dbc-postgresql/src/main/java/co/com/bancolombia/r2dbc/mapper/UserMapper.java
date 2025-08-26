package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.User;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "role.id", target = "roleId")
    UserEntity toEntity(User model);

    @Mapping(source = "roleId", target = "role.id")
    User toModel(UserEntity entity);
}
