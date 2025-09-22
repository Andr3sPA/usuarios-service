package co.com.bancolombia.api.filter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


import org.springframework.web.reactive.function.server.HandlerFunction;

class GlobalExceptionFilterTest {
    @Test
    void testFilterHandlesException() {
        GlobalExceptionFilter filter = new GlobalExceptionFilter();
        ServerRequest request = Mockito.mock(ServerRequest.class);
        HandlerFunction<ServerResponse> next = Mockito.mock(HandlerFunction.class);
        ServerResponse response = Mockito.mock(ServerResponse.class);
        Mockito.when(next.handle(Mockito.any())).thenReturn(Mono.just(response));
        Mono<ServerResponse> result = filter.filter(request, next);
        assert result != null;
    }

    @Test
    void testFilterHandlesBaseException() {
        GlobalExceptionFilter filter = new GlobalExceptionFilter();
        ServerRequest request = Mockito.mock(ServerRequest.class);
        HandlerFunction<ServerResponse> next = Mockito.mock(HandlerFunction.class);
        co.com.bancolombia.exception.BaseException baseException = Mockito.mock(co.com.bancolombia.exception.BaseException.class);
        Mockito.when(baseException.getErrorCode()).thenReturn("ERR");
        Mockito.when(baseException.getMessage()).thenReturn("Error");
        Mockito.when(baseException.getErrors()).thenReturn(java.util.Collections.emptyList());
        Mockito.when(baseException.getStatus()).thenReturn(400);
        Mockito.when(baseException.getTitle()).thenReturn("Titulo");
        Mockito.when(baseException.getTimestamp()).thenReturn(java.time.Instant.now());
        Mockito.when(next.handle(Mockito.any())).thenReturn(Mono.error(baseException));
        Mono<ServerResponse> result = filter.filter(request, next);
        ServerResponse serverResponse = result.block();
        assert serverResponse != null;
    }
}
