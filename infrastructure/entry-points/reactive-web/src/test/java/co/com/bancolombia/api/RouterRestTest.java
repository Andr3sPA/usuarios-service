package co.com.bancolombia.api;

import org.junit.jupiter.api.Test;



import co.com.bancolombia.api.config.LoanAppPath;
import co.com.bancolombia.api.config.UserPath;
import co.com.bancolombia.api.config.ReportsPath;
import co.com.bancolombia.api.filter.GlobalExceptionFilter;
import co.com.bancolombia.api.filter.JwtAuthenticationFilter;
import co.com.bancolombia.api.handler.HandlerSolicitud;
import co.com.bancolombia.api.handler.HandlerUser;
import co.com.bancolombia.api.handler.HandlerAuth;
import co.com.bancolombia.api.handler.HandlerProtected;
import co.com.bancolombia.api.handler.HandlerReporte;
import org.mockito.Mockito;

class RouterRestTest {
    @Test
    void testConstructor() {
        LoanAppPath loanAppPath = Mockito.mock(LoanAppPath.class);
        UserPath userPath = Mockito.mock(UserPath.class);
        ReportsPath reportsPath = Mockito.mock(ReportsPath.class);
        GlobalExceptionFilter globalExceptionFilter = Mockito.mock(GlobalExceptionFilter.class);
        JwtAuthenticationFilter jwtAuthenticationFilter = Mockito.mock(JwtAuthenticationFilter.class);
        RouterRest router = new RouterRest(loanAppPath, userPath, reportsPath, globalExceptionFilter, jwtAuthenticationFilter);
        assert router != null;
    }

    @Test
    void testRouterFunction() {
        LoanAppPath loanAppPath = Mockito.mock(LoanAppPath.class);
        UserPath userPath = Mockito.mock(UserPath.class);
        ReportsPath reportsPath = Mockito.mock(ReportsPath.class);
        GlobalExceptionFilter globalExceptionFilter = Mockito.mock(GlobalExceptionFilter.class);
        JwtAuthenticationFilter jwtAuthenticationFilter = Mockito.mock(JwtAuthenticationFilter.class);
        HandlerSolicitud handlerSolicitud = Mockito.mock(HandlerSolicitud.class);
        HandlerUser handlerUser = Mockito.mock(HandlerUser.class);
        HandlerAuth handlerAuth = Mockito.mock(HandlerAuth.class);
        HandlerProtected handlerProtected = Mockito.mock(HandlerProtected.class);
        HandlerReporte handlerReporte = Mockito.mock(HandlerReporte.class);
        RouterRest router = new RouterRest(loanAppPath, userPath, reportsPath, globalExceptionFilter, jwtAuthenticationFilter);
        Mockito.when(userPath.getRegister()).thenReturn("/api/v1/usuarios");
        Mockito.when(userPath.getLogin()).thenReturn("/api/v1/login");
        Mockito.when(userPath.getLogout()).thenReturn("/api/v1/logout");
        Mockito.when(userPath.getSession()).thenReturn("/api/v1/session");
        Mockito.when(loanAppPath.getLoanApplication()).thenReturn("/api/v1/solicitud");
        Mockito.when(loanAppPath.getLoanCapacity()).thenReturn("/api/v1/capacity");
        Mockito.when(reportsPath.getReports()).thenReturn("/api/v1/reportes");
        assert router.routerFunction(handlerSolicitud, handlerUser, handlerAuth, handlerProtected, handlerReporte) != null;
    }
}
