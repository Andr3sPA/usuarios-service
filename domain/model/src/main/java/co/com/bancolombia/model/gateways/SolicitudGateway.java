
package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.User;
import reactor.core.publisher.Mono;

public interface SolicitudGateway {

    /**
     * Crea una solicitud en el microservicio externo
     * @param solicitudData Datos de la solicitud
     * @param authenticatedUser Usuario autenticado que hace la solicitud
     * @return Mono con la respuesta del microservicio
     */
    <T, R> Mono<R> createSolicitud(T solicitudData, User authenticatedUser, Class<R> responseType);

    /**
     * Obtiene una solicitud por ID desde el microservicio externo
     * @return Mono con la solicitud encontrada
     */
    <R> Mono<R> getSolicitudes( Class<R> responseType,int page,int size);
}