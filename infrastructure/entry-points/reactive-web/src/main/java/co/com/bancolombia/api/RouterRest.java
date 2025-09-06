package co.com.bancolombia.api;

import co.com.bancolombia.api.config.LoanAppPath;
import co.com.bancolombia.api.config.UserPath;
import co.com.bancolombia.api.filter.GlobalExceptionFilter;
import co.com.bancolombia.api.filter.JwtAuthenticationFilter;
import co.com.bancolombia.api.handler.HandlerAuth;
import co.com.bancolombia.api.handler.HandlerProtected;
import co.com.bancolombia.api.handler.HandlerSolicitud;
import co.com.bancolombia.api.handler.HandlerUser;
import co.com.bancolombia.dto.LoginRequest;
import co.com.bancolombia.dto.LoginResponse;
import co.com.bancolombia.dto.UserRegisterRequest;
import co.com.bancolombia.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {
    private final LoanAppPath loanAppPath;
    private final UserPath userPath;
    private final GlobalExceptionFilter globalExceptionFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.POST,
                    beanClass = HandlerUser.class,
                    beanMethod = "registerUser",
                    operation = @Operation(
                            operationId = "registerUser",
                            summary = "Registrar un usuario",
                            description = "Crea un nuevo usuario en el sistema",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = UserRegisterRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario creado exitosamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = User.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Datos inválidos"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    method = RequestMethod.POST,
                    beanClass = HandlerAuth.class,
                    beanMethod = "login",
                    operation = @Operation(
                            operationId = "login",
                            summary = "Iniciar sesión",
                            description = "Autentica un usuario y establece cookie JWT",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = LoginRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Login exitoso",
                                            content = @Content(
                                                    schema = @Schema(implementation = LoginResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Credenciales inválidas"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/logout",
                    method = RequestMethod.POST,
                    beanClass = HandlerAuth.class,
                    beanMethod = "logout",
                    operation = @Operation(
                            operationId = "logout",
                            summary = "Cerrar sesión",
                            description = "Invalida la cookie JWT del usuario"
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    method = RequestMethod.GET,
                    beanClass = HandlerSolicitud.class,
                    beanMethod = "createSolicitud",
                    operation = @Operation(
                            operationId = "createSolicitud",
                            summary = "Crear una solicitud",
                            description = "Crea una nueva solicitud en el microservicio externo"
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(HandlerSolicitud handlerSolicitud,
                                                         HandlerUser handlerUser,
                                                         HandlerAuth handlerAuth,
                                                         HandlerProtected handlerProtected) {
        return route(POST(userPath.getRegister()), handlerUser::registerUser)
                .andRoute(POST(userPath.getLogin()), handlerAuth::login)
                .andRoute(POST(userPath.getLogout()), handlerAuth::logout)
                .andRoute(GET(userPath.getSession()),handlerProtected::getProfile)
                .andRoute(GET(loanAppPath.getLoanApplication()), handlerSolicitud::getSolicitudes)
                .andRoute(POST(loanAppPath.getLoanApplication()), handlerSolicitud::createSolicitud)
                .andRoute(PUT(loanAppPath.getLoanApplication()), handlerSolicitud::updateSolicitud)
                .filter(globalExceptionFilter)
                .filter(jwtAuthenticationFilter);
    }
}