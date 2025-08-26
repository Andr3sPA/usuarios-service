package test.java.co.com.bancolombia.dto;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRegisterRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private UserRegisterRequest buildValidRequest() {
        UserRegisterRequest req = new UserRegisterRequest();
        req.setNombres("Juan");
        req.setApellidos("Pérez");
        req.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        req.setDireccion("Calle 123");
        req.setTelefono("123456789");
        req.setCorreoElectronico("juan.perez@email.com");
        req.setSalarioBase(1000.0);
        req.setPassword("Abcdef1!");
        return req;
    }

    @Test
    void validRequestShouldPass() {
        UserRegisterRequest req = buildValidRequest();
        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

    @Test
    void blankNombreShouldFail() {
        UserRegisterRequest req = buildValidRequest();
        req.setNombres("");
        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    void invalidEmailShouldFail() {
        UserRegisterRequest req = buildValidRequest();
        req.setCorreoElectronico("correo-invalido");
        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shortPasswordShouldFail() {
        UserRegisterRequest req = buildValidRequest();
        req.setPassword("Abc1!");
        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    void passwordWithoutSpecialCharShouldFail() {
        UserRegisterRequest req = buildValidRequest();
        req.setPassword("Abcdef12");
        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    void negativeSalaryShouldFail() {
        UserRegisterRequest req = buildValidRequest();
        req.setSalarioBase(-100.0);
        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    void futureBirthDateShouldFail() {
        UserRegisterRequest req = buildValidRequest();
        req.setFechaNacimiento(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }
}
