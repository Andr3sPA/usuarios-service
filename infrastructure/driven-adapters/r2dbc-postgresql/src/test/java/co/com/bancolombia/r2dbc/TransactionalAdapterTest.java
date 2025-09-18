package co.com.bancolombia.r2dbc;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class TransactionalAdapterTest {
    @Test
    void transactional_shouldReturnMono() {
        org.springframework.transaction.reactive.TransactionalOperator operator = org.mockito.Mockito.mock(org.springframework.transaction.reactive.TransactionalOperator.class);
        Mono<String> mono = Mono.just("test");
    org.mockito.Mockito.when(operator.transactional(mono)).thenReturn(mono);
        TransactionalAdapter adapter = new TransactionalAdapter(operator);
        StepVerifier.create(adapter.transactional(mono))
                .expectNext("test")
                .verifyComplete();
    }

    @Test
    void transactional_shouldHandleError() {
        org.springframework.transaction.reactive.TransactionalOperator operator = org.mockito.Mockito.mock(org.springframework.transaction.reactive.TransactionalOperator.class);
        Mono<String> mono = Mono.error(new RuntimeException("error"));
    org.mockito.Mockito.when(operator.transactional(mono)).thenReturn(mono);
        TransactionalAdapter adapter = new TransactionalAdapter(operator);
        StepVerifier.create(adapter.transactional(mono))
                .expectError(RuntimeException.class)
                .verify();
    }
}
