package uniquindio.edu.co.parcial3.model;

/**
 * Contrato común para las empresas externas de domicilio.
 * Permite al restaurante solicitar el servicio a cualquier empresa con el mismo procedimiento.
 */
public interface IEmpresaDomicilio {

    /** @return nombre de la empresa */
    String getNombre();

    /** @return true si la empresa acepta un recorrido de esa distancia */
    boolean aceptaRecorrido(double distanciaKm);

    /** @return costo del domicilio para esa distancia */
    double calcularCosto(double distanciaKm);

    /**
     * Solicita el servicio de domicilio.
     * @return el Domicilio aceptado con su costo
     * @throws IllegalArgumentException si la empresa no acepta el recorrido
     */
    Domicilio solicitarServicio(double distanciaKm);
}
