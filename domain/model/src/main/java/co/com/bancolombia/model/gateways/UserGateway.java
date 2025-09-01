package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.User;
import reactor.core.publisher.Mono;

public interface UserGateway {
    Mono<User> register(User user);
    Mono<User> findByEmail(String email);
}
