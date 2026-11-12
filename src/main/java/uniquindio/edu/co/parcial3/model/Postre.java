package uniquindio.edu.co.parcial3.model;

/** Postre: incremento del 15 % y 5 % adicional sobre el precio base si es refrigerado. */
public class Postre extends Producto {
    private double pesoGramos;
    private double temperaturaAlmacenamiento;
    private boolean requiereRefrigeracion;

    /**
     * Constructor de la clase Postre.
     * @param pesoGramos peso en gramos
     * @param temperaturaAlmacenamiento temperatura de almacenamiento en °C
     * @param requiereRefrigeracion true si requiere refrigeración
     */
    public Postre(String codigo, String nombre, String descripcion, double precioBase, int tiempoPreparacionMin,
                  double pesoGramos, double temperaturaAlmacenamiento, boolean requiereRefrigeracion) {
        super(codigo, nombre, descripcion, precioBase, tiempoPreparacionMin);
        this.pesoGramos = pesoGramos;
        this.temperaturaAlmacenamiento = temperaturaAlmacenamiento;
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    /** Precio de venta = precio base + 15 % (+ 5 % del precio base si es refrigerado). */
    @Override
    public double calcularPrecioVenta() {
        double precio = getPrecioBase() * 1.15;
        if (requiereRefrigeracion) {
            precio += getPrecioBase() * 0.05;
        }
        return precio;
    }

    /** Peso entre 80 y 500 g; refrigerado entre 0 y 6 °C, no refrigerado entre 15 y 30 °C. */
    @Override
    public boolean cumpleCalidad() {
        boolean pesoOk = pesoGramos >= 80 && pesoGramos <= 500;
        boolean temperaturaOk;
        if (requiereRefrigeracion) {
            temperaturaOk = temperaturaAlmacenamiento >= 0 && temperaturaAlmacenamiento <= 6;
        } else {
            temperaturaOk = temperaturaAlmacenamiento >= 15 && temperaturaAlmacenamiento <= 30;
        }
        return pesoOk && temperaturaOk;
    }

    @Override
    public CategoriaProducto obtenerCategoria() {
        return CategoriaProducto.POSTRE;
    }

    public double getPesoGramos() { return pesoGramos; }
    public void setPesoGramos(double pesoGramos) { this.pesoGramos = pesoGramos; }
    public double getTemperaturaAlmacenamiento() { return temperaturaAlmacenamiento; }
    public void setTemperaturaAlmacenamiento(double temperaturaAlmacenamiento) { this.temperaturaAlmacenamiento = temperaturaAlmacenamiento; }
    public boolean isRequiereRefrigeracion() { return requiereRefrigeracion; }
    public void setRequiereRefrigeracion(boolean requiereRefrigeracion) { this.requiereRefrigeracion = requiereRefrigeracion; }
}
