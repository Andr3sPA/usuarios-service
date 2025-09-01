package co.com.bancolombia.usecase.auth;

import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.AuthenticationGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class AuthUseCase {

    private final AuthenticationGateway authGateway;

    public Mono<String> login(String email, String password) {
        return authGateway.authenticateUser(email,password);
    }

    public Mono<User> validateTokenAndGetUser(String token) {
        return authGateway.validateTokenAndGetUser(token);
    }

}