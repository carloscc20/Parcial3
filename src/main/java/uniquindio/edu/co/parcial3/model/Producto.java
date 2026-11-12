package uniquindio.edu.co.parcial3.model;

/**
 * Clase abstracta que representa un producto del menú.
 * Cada categoría (plato fuerte, bebida, postre) calcula su propio
 * precio de venta y verifica sus propias condiciones de calidad.
 */
public abstract class Producto {
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precioBase;
    private int tiempoPreparacionMin;

    /**
     * Constructor de la clase Producto.
     * @param codigo código único del producto
     * @param nombre nombre del producto
     * @param descripcion descripción del producto
     * @param precioBase precio base del producto
     * @param tiempoPreparacionMin tiempo estimado de preparación en minutos
     */
    public Producto(String codigo, String nombre, String descripcion, double precioBase, int tiempoPreparacionMin) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.tiempoPreparacionMin = tiempoPreparacionMin;
    }

    /** @return precio de venta según las reglas de la categoría */
    public abstract double calcularPrecioVenta();

    /** @return true si el producto cumple las condiciones de calidad */
    public abstract boolean cumpleCalidad();

    /** @return categoría a la que pertenece el producto */
    public abstract CategoriaProducto obtenerCategoria();

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
    public int getTiempoPreparacionMin() { return tiempoPreparacionMin; }
    public void setTiempoPreparacionMin(int tiempoPreparacionMin) { this.tiempoPreparacionMin = tiempoPreparacionMin; }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + obtenerCategoria() + ") | Precio venta: $"
                + String.format("%,.0f", calcularPrecioVenta())
                + " | Calidad: " + (cumpleCalidad() ? "OK" : "NO CUMPLE");
    }
}
