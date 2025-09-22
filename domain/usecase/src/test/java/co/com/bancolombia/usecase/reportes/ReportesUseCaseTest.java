package co.com.bancolombia.usecase.reportes;

import org.junit.jupiter.api.Test;
import co.com.bancolombia.model.gateways.ReportesGateway;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReportesUseCaseTest {
    @Test
    void emptyClass_shouldInstantiate() {
        ReportesGateway gateway = Mockito.mock(ReportesGateway.class);
        ReportesUseCase useCase = new ReportesUseCase(gateway);
        assert useCase != null;
    }
}
