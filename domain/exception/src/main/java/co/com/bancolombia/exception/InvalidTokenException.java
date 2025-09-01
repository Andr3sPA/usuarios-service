package co.com.bancolombia.exception;


import java.util.List;

public class InvalidTokenException extends BaseException {
    public InvalidTokenException(String details) {
        super(
                "INVALID_TOKEN",
                "Token inválido",
                "El token proporcionado no es válido o ha expirado",
                List.of("Token no válido: " + details),
                401
        );
    }
}