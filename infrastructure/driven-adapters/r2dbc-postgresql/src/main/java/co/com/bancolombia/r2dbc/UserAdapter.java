package co.com.bancolombia.r2dbc;
import lombok.extern.slf4j.Slf4j;

import co.com.bancolombia.exception.EmailAlreadyExistsException;
import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.UserGateway;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import co.com.bancolombia.r2dbc.mapper.RoleMapper;
import co.com.bancolombia.r2dbc.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserAdapter implements UserGateway {

    private final ReactiveUserRepository userRepo;
    private final ReactiveRoleRepository roleRepo;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Mono<User> register(User user) {
        log.info("Intentando registrar usuario con email: {}", user.getEmail());
        return userRepo.findByEmail(user.getEmail())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        log.warn("El email {} ya está registrado", user.getEmail());
                        return Mono.error(new EmailAlreadyExistsException(user.getEmail()));
                    }

                    return roleRepo.findById(user.getRole().getId())
                            .map(roleMapper::toModel)
                            .flatMap(role -> {
                                user.setRole(role);
                                user.setPassword(passwordEncoder.encode(user.getPassword()));

                                UserEntity entity = userMapper.toEntity(user);
                                log.info("Guardando usuario en base de datos: {}", entity.getEmail());
                                return userRepo.save(entity);
                            })
                            .map(userMapper::toModel)
                            .doOnSuccess(u -> log.info("Usuario registrado exitosamente: {}", u.getEmail()))
                            .doOnError(e -> log.error("Error al registrar usuario", e));
                });
    }


    @Override
    public Mono<User> findByEmail(String email) {
        log.info("Buscando usuario por email: {}", email);
        return userRepo.findByEmail(email)
                .flatMap(entity ->
                        roleRepo.findById(entity.getRoleId())
                                .map(role -> {
                                    User model = userMapper.toModel(entity);
                                    model.setRole(roleMapper.toModel(role));
                                    log.info("Usuario encontrado: {}", model.getEmail());
                                    return model;
                                })
                )
                .doOnError(e -> log.error("Error al buscar usuario por email", e));
    }
}
