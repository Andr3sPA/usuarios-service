package co.com.bancolombia.r2dbc;
import co.com.bancolombia.exception.EmailAlreadyExistsException;
import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.UserRepository;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import co.com.bancolombia.r2dbc.mapper.RoleMapper;
import co.com.bancolombia.r2dbc.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final ReactiveUserRepository userRepo;
    private final ReactiveRoleRepository roleRepo;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Mono<User> register(User user) {
        return userRepo.findByEmail(user.getEmail())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new EmailAlreadyExistsException(user.getEmail()));
                    }

                    return roleRepo.findById(user.getRole().getId())
                            .map(roleMapper::toModel)
                            .flatMap(role -> {
                                user.setRole(role);
                                user.setPassword(passwordEncoder.encode(user.getPassword()));

                                UserEntity entity = userMapper.toEntity(user);
                                return userRepo.save(entity);
                            })
                            .map(userMapper::toModel);
                });
    }


    @Override
    public Mono<User> findByEmail(String email) {
        return userRepo.findByEmail(email)
                .flatMap(entity ->
                        roleRepo.findById(entity.getRoleId())
                                .map(role -> {
                                    User model = userMapper.toModel(entity);
                                    model.setRole(roleMapper.toModel(role));
                                    return model;
                                })
                );
    }
}
