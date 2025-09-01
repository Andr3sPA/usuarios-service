package co.com.bancolombia.usecase.user;
import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.UserGateway;
import co.com.bancolombia.model.gateways.TransactionalGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class UserUseCase {
    private final UserGateway repository;
    private final TransactionalGateway transactionalGateway;

    public Mono<User> register(User user) {
        return transactionalGateway.transactional(
            repository.register(user)
        );
    }
}
