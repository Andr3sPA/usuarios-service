package co.com.bancolombia.api;

import co.com.bancolombia.api.config.LoanAppPath;
import co.com.bancolombia.api.config.ReportsPath;
import co.com.bancolombia.api.config.UserPath;
import co.com.bancolombia.api.filter.GlobalExceptionFilter;
import co.com.bancolombia.api.filter.JwtAuthenticationFilter;
import co.com.bancolombia.api.handler.*;
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
    private final ReportsPath reportsPath;
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
                                description = "Invalida la cookie JWT del usuario",
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Sesión cerrada exitosamente"
                                        ),
                                        @ApiResponse(
                                                responseCode = "401",
                                                description = "No autenticado"
                                        )
                                }
                        )
                ),
                @RouterOperation(
                        path = "/api/v1/solicitud",
                        method = RequestMethod.GET,
                        beanClass = HandlerSolicitud.class,
                        beanMethod = "getSolicitudes",
                        operation = @Operation(
                                operationId = "getSolicitudes",
                                summary = "Obtener solicitudes",
                                description = "Obtiene todas las solicitudes del usuario",
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Listado de solicitudes"
                                        ),
                                        @ApiResponse(
                                                responseCode = "401",
                                                description = "No autenticado"
                                        )
                                }
                        )
                ),
                @RouterOperation(
                        path = "/api/v1/solicitud",
                        method = RequestMethod.POST,
                        beanClass = HandlerSolicitud.class,
                        beanMethod = "createSolicitud",
                        operation = @Operation(
                                operationId = "createSolicitud",
                                summary = "Crear una solicitud",
                                description = "Crea una nueva solicitud en el microservicio externo",
                                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                        required = true,
                                        content = @Content(
                                                schema = @Schema(implementation = Object.class)
                                        )
                                ),
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Solicitud creada exitosamente"
                                        ),
                                        @ApiResponse(
                                                responseCode = "400",
                                                description = "Datos inválidos"
                                        )
                                }
                        )
                ),
                @RouterOperation(
                        path = "/api/v1/solicitud",
                        method = RequestMethod.PUT,
                        beanClass = HandlerSolicitud.class,
                        beanMethod = "updateSolicitud",
                        operation = @Operation(
                                operationId = "updateSolicitud",
                                summary = "Actualizar solicitud",
                                description = "Actualiza una solicitud existente",
                                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                        required = true,
                                        content = @Content(
                                                schema = @Schema(implementation = Object.class)
                                        )
                                ),
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Solicitud actualizada exitosamente"
                                        ),
                                        @ApiResponse(
                                                responseCode = "404",
                                                description = "Solicitud no encontrada"
                                        )
                                }
                        )
                ),
                @RouterOperation(
                        path = "/api/v1/solicitud/capacity",
                        method = RequestMethod.POST,
                        beanClass = HandlerSolicitud.class,
                        beanMethod = "calculateCapacity",
                        operation = @Operation(
                                operationId = "calculateCapacity",
                                summary = "Calcular capacidad de solicitud",
                                description = "Calcula la capacidad de préstamo para el usuario",
                                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                        required = true,
                                        content = @Content(
                                                schema = @Schema(implementation = Object.class)
                                        )
                                ),
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Capacidad calculada exitosamente"
                                        ),
                                        @ApiResponse(
                                                responseCode = "400",
                                                description = "Datos inválidos"
                                        )
                                }
                        )
                ),
                @RouterOperation(
                        path = "/api/v1/session",
                        method = RequestMethod.GET,
                        beanClass = HandlerProtected.class,
                        beanMethod = "getProfile",
                        operation = @Operation(
                                operationId = "getProfile",
                                summary = "Obtener perfil de usuario",
                                description = "Devuelve el perfil del usuario autenticado",
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Perfil obtenido exitosamente",
                                                content = @Content(
                                                        schema = @Schema(implementation = Object.class)
                                                )
                                        ),
                                        @ApiResponse(
                                                responseCode = "401",
                                                description = "No autenticado"
                                        )
                                }
                        )
                ),
                @RouterOperation(
                        path = "/api/v1/reportes",
                        method = RequestMethod.GET,
                        beanClass = HandlerReporte.class,
                        beanMethod = "getReportes",
                        operation = @Operation(
                                operationId = "getReportes",
                                summary = "Obtener reportes",
                                description = "Devuelve los reportes disponibles para el usuario",
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Listado de reportes"
                                        ),
                                        @ApiResponse(
                                                responseCode = "204",
                                                description = "Sin contenido"
                                        ),
                                        @ApiResponse(
                                                responseCode = "401",
                                                description = "No autenticado"
                                        )
                                }
                        )
                )
        })
    public RouterFunction<ServerResponse> routerFunction(HandlerSolicitud handlerSolicitud,
                                                         HandlerUser handlerUser,
                                                         HandlerAuth handlerAuth,
                                                         HandlerProtected handlerProtected, HandlerReporte handlerReporte) {
        return route(POST(userPath.getRegister()), handlerUser::registerUser)
                .andRoute(POST(userPath.getLogin()), handlerAuth::login)
                .andRoute(POST(userPath.getLogout()), handlerAuth::logout)
                .andRoute(GET(userPath.getSession()),handlerProtected::getProfile)
                .andRoute(GET(loanAppPath.getLoanApplication()), handlerSolicitud::getSolicitudes)
                .andRoute(POST(loanAppPath.getLoanApplication()), handlerSolicitud::createSolicitud)
                .andRoute(PUT(loanAppPath.getLoanApplication()), handlerSolicitud::updateSolicitud)
                .andRoute(POST(loanAppPath.getLoanCapacity()), handlerSolicitud::calculateCapacity)
                .andRoute(GET(reportsPath.getReports()), handlerReporte::getReportes)
                .filter(globalExceptionFilter)
                .filter(jwtAuthenticationFilter);
    }
}