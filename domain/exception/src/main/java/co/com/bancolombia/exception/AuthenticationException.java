package co.com.bancolombia.exception;

import java.util.List;

public class AuthenticationException extends BaseException {

    public AuthenticationException(String message) {
        super(
                "AUTHENTICATION_ERROR",
                "Error de autenticación",
                message,
                List.of("No se pudo validar la autenticación"),
                401
        );
    }
}
