package co.com.bancolombia.api;

import co.com.bancolombia.api.config.UserPath;
import co.com.bancolombia.api.handler.HandlerUser;
import co.com.bancolombia.dto.UserRegisterRequest;
import co.com.bancolombia.model.Role;
import co.com.bancolombia.model.User;
import co.com.bancolombia.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, HandlerUser.class})
@EnableConfigurationProperties(UserPath.class)
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;

    @Autowired
    private UserPath userPath;

    @Test
    void shouldRegisterUser() {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phone("1234567890")
                .email("john.doe@example.com")
                .roleId(1L)
                .baseSalary(3000.0)
                .password("Secure123!")
                .build();

        User response = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phone("1234567890")
                .email("john.doe@example.com")
                .role(Role.builder().id(1L).name("ADMIN").description("Administrator role").build())
                .baseSalary(3000.0)
                .password("hashedPassword")
                .build();

        when(userUseCase.register(any(User.class))).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri(userPath.getRegister())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .consumeWith(result -> {
                    User user = result.getResponseBody();
                    assert user != null;
                    org.assertj.core.api.Assertions.assertThat(user.getId()).isEqualTo(1L);
                    org.assertj.core.api.Assertions.assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
                    org.assertj.core.api.Assertions.assertThat(user.getRole().getId()).isEqualTo(1L);
                });
    }

    @Test
    void shouldReturnBadRequestWhenInvalidRequest() {
        UserRegisterRequest invalidRequest = UserRegisterRequest.builder()
                .firstName("") // inválido
                .lastName("D") // inválido (min 2 chars)
                .birthDate(LocalDate.now().plusDays(1)) // inválido (future)
                .address("x") // inválido
                .phone("abc") // inválido
                .email("bad-email") // inválido
                .roleId(null) // inválido
                .baseSalary(-100.0) // inválido
                .password("123") // inválido
                .build();

        webTestClient.post()
                .uri(userPath.getRegister())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }
}

