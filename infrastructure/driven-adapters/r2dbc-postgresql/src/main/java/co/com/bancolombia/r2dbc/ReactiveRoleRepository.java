package co.com.bancolombia.r2dbc;

import co.com.bancolombia.r2dbc.entity.RoleEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface ReactiveRoleRepository extends ReactiveCrudRepository<RoleEntity, Long> {
}
