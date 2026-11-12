package uniquindio.edu.co.parcial3.model;

/** Plato fuerte: incremento del 20 % sobre el precio base. */
public class PlatoFuerte extends Producto {
    private double pesoGramos;
    private TipoPreparacion tipoPreparacion;
    private boolean coccionAprobada;

    /**
     * Constructor de la clase PlatoFuerte.
     * @param pesoGramos peso en gramos
     * @param tipoPreparacion asado, frito, horneado o a la plancha
     * @param coccionAprobada true si superó el control de cocción
     */
    public PlatoFuerte(String codigo, String nombre, String descripcion, double precioBase, int tiempoPreparacionMin,
                       double pesoGramos, TipoPreparacion tipoPreparacion, boolean coccionAprobada) {
        super(codigo, nombre, descripcion, precioBase, tiempoPreparacionMin);
        this.pesoGramos = pesoGramos;
        this.tipoPreparacion = tipoPreparacion;
        this.coccionAprobada = coccionAprobada;
    }

    /** Precio de venta = precio base + 20 %. */
    @Override
    public double calcularPrecioVenta() {
        return getPrecioBase() * 1.20;
    }

    /** Peso entre 200 y 900 g, tiempo entre 10 y 60 min y cocción aprobada. */
    @Override
    public boolean cumpleCalidad() {
        return pesoGramos >= 200 && pesoGramos <= 900
                && getTiempoPreparacionMin() >= 10 && getTiempoPreparacionMin() <= 60
                && coccionAprobada;
    }

    @Override
    public CategoriaProducto obtenerCategoria() {
        return CategoriaProducto.PLATO_FUERTE;
    }

    public double getPesoGramos() { return pesoGramos; }
    public void setPesoGramos(double pesoGramos) { this.pesoGramos = pesoGramos; }
    public TipoPreparacion getTipoPreparacion() { return tipoPreparacion; }
    public void setTipoPreparacion(TipoPreparacion tipoPreparacion) { this.tipoPreparacion = tipoPreparacion; }
    public boolean isCoccionAprobada() { return coccionAprobada; }
    public void setCoccionAprobada(boolean coccionAprobada) { this.coccionAprobada = coccionAprobada; }
}
