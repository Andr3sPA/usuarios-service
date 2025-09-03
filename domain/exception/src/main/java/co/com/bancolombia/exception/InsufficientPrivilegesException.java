package co.com.bancolombia.exception;

import java.util.List;

public class InsufficientPrivilegesException extends BaseException {
    public InsufficientPrivilegesException() {
        super(
                "INSUFFICIENT_PRIVILEGES",
                "Privilegios insuficientes",
                "No tienes permisos suficientes para acceder a este recurso",
                List.of("Se requieren permisos de administrador para esta operación"),
                403
        );
    }

    public InsufficientPrivilegesException(String requiredRole) {
        super(
                "INSUFFICIENT_PRIVILEGES",
                "Privilegios insuficientes",
                "Se requiere el rol " + requiredRole + " para acceder a este recurso",
                List.of("Rol actual insuficiente para esta operación"),
                403
        );
    }
}