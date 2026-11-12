package uniquindio.edu.co.parcial3.model;

/** EntregaSegura: $7.000 + $1.000 por km, + $3.000 si supera 5 km, recorridos de hasta 20 km. */
public class EntregaSegura implements IEmpresaDomicilio {
    public static final double TARIFA_BASE = 7000;
    public static final double VALOR_KM = 1000;
    public static final double RECARGO_DISTANCIA = 3000;
    public static final double KM_PARA_RECARGO = 5;
    public static final double DISTANCIA_MAXIMA = 20;

    private String nombre;

    /** Constructor de la clase EntregaSegura. */
    public EntregaSegura() {
        this.nombre = "EntregaSegura";
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean aceptaRecorrido(double distanciaKm) {
        return distanciaKm > 0 && distanciaKm <= DISTANCIA_MAXIMA;
    }

    @Override
    public double calcularCosto(double distanciaKm) {
        double costo = TARIFA_BASE + VALOR_KM * distanciaKm;
        if (distanciaKm > KM_PARA_RECARGO) {
            costo += RECARGO_DISTANCIA;
        }
        return costo;
    }

    @Override
    public Domicilio solicitarServicio(double distanciaKm) {
        if (!aceptaRecorrido(distanciaKm)) {
            throw new IllegalArgumentException(nombre + " no acepta recorridos de " + distanciaKm + " km (máximo " + DISTANCIA_MAXIMA + " km)");
        }
        return new Domicilio(this, distanciaKm, calcularCosto(distanciaKm));
    }

    @Override
    public String toString() {
        return nombre;
    }
}
