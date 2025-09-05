package co.com.bancolombia.usecase.solicitud;


import co.com.bancolombia.model.User;
import co.com.bancolombia.model.gateways.SolicitudGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudGateway solicitudGateway;

    /**
     * Crea una nueva solicitud
     * @param solicitudData Datos de la solicitud
     * @param authenticatedUser Usuario autenticado
     * @return Mono con la respuesta de la solicitud creada
     */
    public <T, R> Mono<R> createSolicitud(T solicitudData, User authenticatedUser, Class<R> responseType) {

        return solicitudGateway.createSolicitud(solicitudData, authenticatedUser, responseType);
    }

    /**
     * Obtiene una solicitud por ID
     * @return Mono con la solicitud encontrada
     */
    public <R> Mono<R> getSolicitudes( Class<R> responseType,int page,int size) {

        return solicitudGateway.getSolicitudes( responseType,page,size);
    }
}