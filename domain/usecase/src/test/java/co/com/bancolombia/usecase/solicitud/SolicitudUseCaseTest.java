package co.com.bancolombia.usecase.solicitud;

import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.SolicitudGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class SolicitudUseCaseTest {

    @Mock
    private SolicitudGateway solicitudGateway;

    @InjectMocks
    private SolicitudUseCase solicitudUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSolicitud() {
        Object solicitudData = new Object();
        User user = User.builder().id(1L).email("test@example.com").build();
        Object response = new Object();

        when(solicitudGateway.createSolicitud(any(), any(User.class), any())).thenReturn(Mono.just(response));

        StepVerifier.create(solicitudUseCase.createSolicitud(solicitudData, user, Object.class))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testGetSolicitudes() {
        Object response = new Object();

        when(solicitudGateway.getSolicitudes(any(), anyInt(), anyInt())).thenReturn(Mono.just(response));

        StepVerifier.create(solicitudUseCase.getSolicitudes(Object.class, 0, 10))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testUpdateSolicitud() {
        Object solicitudData = new Object();
        Object response = new Object();

        when(solicitudGateway.updateSolicitud(any(), any())).thenReturn(Mono.just(response));

        StepVerifier.create(solicitudUseCase.updateSolicitud(solicitudData, Object.class))
                .expectNext(response)
                .verifyComplete();
    }
}
