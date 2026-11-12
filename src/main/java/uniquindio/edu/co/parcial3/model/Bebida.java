package uniquindio.edu.co.parcial3.model;

/** Bebida: incremento del 10 % y recargo fijo de $3.000 si requiere preparación especial. */
public class Bebida extends Producto {
    public static final double RECARGO_PREPARACION_ESPECIAL = 3000;

    private double volumenMl;
    private double temperaturaServicio;
    private TipoTemperatura tipoTemperatura;
    private boolean requierePreparacionEspecial;

    /**
     * Constructor de la clase Bebida.
     * @param volumenMl volumen en mililitros
     * @param temperaturaServicio temperatura de servicio en °C
     * @param tipoTemperatura FRIA o CALIENTE
     * @param requierePreparacionEspecial true si tiene preparación especial
     */
    public Bebida(String codigo, String nombre, String descripcion, double precioBase, int tiempoPreparacionMin,
                  double volumenMl, double temperaturaServicio, TipoTemperatura tipoTemperatura,
                  boolean requierePreparacionEspecial) {
        super(codigo, nombre, descripcion, precioBase, tiempoPreparacionMin);
        this.volumenMl = volumenMl;
        this.temperaturaServicio = temperaturaServicio;
        this.tipoTemperatura = tipoTemperatura;
        this.requierePreparacionEspecial = requierePreparacionEspecial;
    }

    /** Precio de venta = precio base + 10 % (+ $3.000 si requiere preparación especial). */
    @Override
    public double calcularPrecioVenta() {
        double precio = getPrecioBase() * 1.10;
        if (requierePreparacionEspecial) {
            precio += RECARGO_PREPARACION_ESPECIAL;
        }
        return precio;
    }

    /** Volumen entre 150 y 1.500 ml; fría entre 0 y 10 °C, caliente entre 55 y 85 °C. */
    @Override
    public boolean cumpleCalidad() {
        boolean volumenOk = volumenMl >= 150 && volumenMl <= 1500;
        boolean temperaturaOk;
        if (tipoTemperatura == TipoTemperatura.FRIA) {
            temperaturaOk = temperaturaServicio >= 0 && temperaturaServicio <= 10;
        } else {
            temperaturaOk = temperaturaServicio >= 55 && temperaturaServicio <= 85;
        }
        return volumenOk && temperaturaOk;
    }

    @Override
    public CategoriaProducto obtenerCategoria() {
        return CategoriaProducto.BEBIDA;
    }

    public double getVolumenMl() { return volumenMl; }
    public void setVolumenMl(double volumenMl) { this.volumenMl = volumenMl; }
    public double getTemperaturaServicio() { return temperaturaServicio; }
    public void setTemperaturaServicio(double temperaturaServicio) { this.temperaturaServicio = temperaturaServicio; }
    public TipoTemperatura getTipoTemperatura() { return tipoTemperatura; }
    public void setTipoTemperatura(TipoTemperatura tipoTemperatura) { this.tipoTemperatura = tipoTemperatura; }
    public boolean isRequierePreparacionEspecial() { return requierePreparacionEspecial; }
    public void setRequierePreparacionEspecial(boolean requierePreparacionEspecial) { this.requierePreparacionEspecial = requierePreparacionEspecial; }
}
