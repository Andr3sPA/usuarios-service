package co.com.bancolombia.exception;

import java.util.List;

public class UserNotAuthenticatedException extends BaseException {
    public UserNotAuthenticatedException() {
        super(
                "USER_NOT_AUTHENTICATED",
                "Usuario no autenticado",
                "No se encontró información del usuario autenticado",
                List.of("Usuario no presente en el contexto"),
                401
        );
    }
}