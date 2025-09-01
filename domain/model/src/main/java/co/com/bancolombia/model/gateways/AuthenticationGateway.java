package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.User;
import reactor.core.publisher.Mono;

public interface AuthenticationGateway {
    Mono<String> authenticateUser(String email, String password);
    Mono<User> validateTokenAndGetUser (String token);
}