package uniquindio.edu.co.parcial3.model;

/**
 * Record inmutable con el servicio de domicilio aceptado por una empresa externa.
 * @param empresa empresa que realiza la entrega
 * @param distanciaKm distancia del recorrido en km
 * @param costo valor cobrado por el domicilio
 */
public record Domicilio(IEmpresaDomicilio empresa, double distanciaKm, double costo) {
}
