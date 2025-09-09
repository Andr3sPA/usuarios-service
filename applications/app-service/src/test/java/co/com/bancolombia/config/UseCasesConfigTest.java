package co.com.bancolombia.config;

import co.com.bancolombia.model.gateways.UserGateway;
import co.com.bancolombia.model.gateways.TransactionalGateway;
import co.com.bancolombia.model.gateways.AuthenticationGateway;
import co.com.bancolombia.model.gateways.SolicitudGateway;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'UseCase' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public UserGateway userRepository() {
            return Mockito.mock(UserGateway.class);
        }

        @Bean
        public TransactionalGateway transactionalGateway() {
            return Mockito.mock(TransactionalGateway.class);
        }

        @Bean
        public AuthenticationGateway authenticationGateway() {
            return Mockito.mock(AuthenticationGateway.class);
        }

        @Bean
        public SolicitudGateway solicitudGateway() {
            return Mockito.mock(SolicitudGateway.class);
        }

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}
