package co.com.bancolombia.api.util;

import org.junit.jupiter.api.Test;



import org.springframework.validation.Validator;
import org.mockito.Mockito;

class RequestValidatorTest {
    static class TestDto {
        private String field;
        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
    }
    @Test
    void testConstructor() {
        Validator validatorMock = Mockito.mock(Validator.class);
        RequestValidator validator = new RequestValidator(validatorMock);
        assert validator != null;
    }

    @Test
    void testValidateSuccess() {
        Validator validatorMock = Mockito.mock(Validator.class);
        RequestValidator validator = new RequestValidator(validatorMock);
        Object dto = new Object();
        validator.validate(dto, Object.class);
    }

    @Test
    void testValidateError() {
        Validator validatorMock = Mockito.mock(Validator.class);
        RequestValidator validator = new RequestValidator(validatorMock);
        TestDto dto = new TestDto();
        Mockito.doAnswer(invocation -> {
            Object arg1 = invocation.getArgument(0);
            org.springframework.validation.Errors arg2 = invocation.getArgument(1);
            ((org.springframework.validation.Errors)arg2).rejectValue("field", "error", "Campo requerido");
            return null;
        }).when(validatorMock).validate(Mockito.any(), Mockito.any());
        try {
            validator.validate(dto, TestDto.class);
            assert false;
        } catch (co.com.bancolombia.exception.MissingFieldException e) {
            assert e.getMessage().equals("El campo 'Campo requerido' es obligatorio");
        }
    }
}
