package co.com.bancolombia.exception;

import java.util.List;

public class TokenNotFoundException extends BaseException {
    public TokenNotFoundException() {
        super(
                "TOKEN_NOT_FOUND",
                "Token no encontrado",
                "No se encontró token de autenticación",
                List.of("Cookie jwt-token no presente"),
                401
        );
    }
}