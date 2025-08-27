package co.com.bancolombia.exception;


import java.util.List;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException(String email) {
        super(
                "EMAIL_ALREADY_EXISTS",
                "Email already exists",
                "El correo " + email + " ya se encuentra registrado",
                List.of("El email ya existe en la base de datos"),
                409
        );
    }
}
