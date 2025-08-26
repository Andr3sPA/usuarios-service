package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.Role;
import co.com.bancolombia.r2dbc.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toModel(RoleEntity entity);
    RoleEntity toEntity(Role model);
}
