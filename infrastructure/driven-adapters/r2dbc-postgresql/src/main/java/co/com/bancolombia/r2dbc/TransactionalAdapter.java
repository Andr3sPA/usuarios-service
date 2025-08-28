package co.com.bancolombia.r2dbc;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import co.com.bancolombia.model.gateways.TransactionalGateway;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@Slf4j
@Component
public class TransactionalAdapter implements TransactionalGateway {
    private final TransactionalOperator operator;


    @Override
    public <T> Mono<T> transactional(Mono<T> action) {
        log.info("Iniciando transacción reactiva");
        return operator.transactional(action)
            .doOnSuccess(result -> log.info("Transacción completada exitosamente"))
            .doOnError(error -> log.error("Error en la transacción", error));
    }
}
