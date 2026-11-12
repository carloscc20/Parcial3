package uniquindio.edu.co.parcial3.model;

/** RápidoExpress: $5.000 + $1.500 por km, recorridos de hasta 12 km. */
public class RapidoExpress implements IEmpresaDomicilio {
    public static final double TARIFA_BASE = 5000;
    public static final double VALOR_KM = 1500;
    public static final double DISTANCIA_MAXIMA = 12;

    private String nombre;

    /** Constructor de la clase RapidoExpress. */
    public RapidoExpress() {
        this.nombre = "RápidoExpress";
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
        return TARIFA_BASE + VALOR_KM * distanciaKm;
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
