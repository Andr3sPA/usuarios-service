package co.com.bancolombia.exception;

import java.util.List;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException() {
        super(
                "INVALID_CREDENTIALS",
                "Credenciales inválidas",
                "Email o contraseña incorrectos",
                List.of("Las credenciales proporcionadas no son válidas"),
                401
        );
    }
}