package uniquindio.edu.co.parcial3.model;

/**
 * Record inmutable con un producto y la cantidad solicitada dentro de un pedido.
 * @param producto producto solicitado
 * @param cantidad cantidad solicitada (mayor que cero)
 */
public record DetallePedido(Producto producto, int cantidad) {

    /** Constructor compacto: valida los datos antes de crear el detalle. */
    public DetallePedido {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }

    /** @return precio de venta del producto × cantidad */
    public double calcularSubtotal() {
        return producto.calcularPrecioVenta() * cantidad;
    }
}
