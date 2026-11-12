package uniquindio.edu.co.parcial3.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Pedido realizado por un cliente, con sus productos, cantidades y domicilio opcional. */
public class Pedido {
    private String codigo;
    private LocalDate fecha;
    private Cliente theCliente;
    private ModalidadEntrega modalidad;
    private EstadoPedido estado;
    private List<DetallePedido> listPedidoDetalles;
    private Domicilio theDomicilio;

    /**
     * Constructor de la clase Pedido. Todo pedido inicia en estado PENDIENTE.
     * @param codigo código único del pedido
     * @param fecha fecha del pedido
     * @param cliente cliente que realiza el pedido
     * @param modalidad consumo en restaurante o domicilio
     */
    public Pedido(String codigo, LocalDate fecha, Cliente cliente, ModalidadEntrega modalidad) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.theCliente = cliente;
        this.modalidad = modalidad;
        this.estado = EstadoPedido.PENDIENTE;
        this.listPedidoDetalles = new ArrayList<>();
    }

    /** Agrega un producto con su cantidad (el record DetallePedido valida cantidad > 0). */
    public void agregarProducto(Producto producto, int cantidad) {
        listPedidoDetalles.add(new DetallePedido(producto, cantidad));
    }

    /**
     * Solicita el domicilio a la empresa elegida (procedimiento común de la interfaz).
     * @throws IllegalArgumentException si el pedido no es a domicilio o la empresa no acepta
     */
    public void asignarDomicilio(IEmpresaDomicilio empresa, double distanciaKm) {
        if (!esADomicilio()) {
            throw new IllegalArgumentException("El pedido no es a domicilio");
        }
        if (empresa == null) {
            throw new IllegalArgumentException("Debe seleccionar una empresa de domicilio");
        }
        this.theDomicilio = empresa.solicitarServicio(distanciaKm);
    }

    /** @return true si la modalidad es DOMICILIO */
    public boolean esADomicilio() {
        return modalidad == ModalidadEntrega.DOMICILIO;
    }

    /** @return suma de los subtotales de los productos */
    public double calcularValorProductos() {
        double total = 0;
        for (DetallePedido detalle : listPedidoDetalles) {
            total += detalle.calcularSubtotal();
        }
        return total;
    }

    /** @return costo del domicilio, o 0 si no aplica */
    public double calcularCostoDomicilio() {
        if (theDomicilio == null) {
            return 0;
        }
        return theDomicilio.costo();
    }

    /** @return valor de los productos + costo del domicilio */
    public double calcularValorTotal() {
        return calcularValorProductos() + calcularCostoDomicilio();
    }

    /** Confirma el pedido. Solo un pedido PENDIENTE puede confirmarse (evita confirmaciones repetidas). */
    public void confirmar() {
        if (estado != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("El pedido " + codigo + " ya está " + estado);
        }
        estado = EstadoPedido.CONFIRMADO;
    }

    /** Marca el pedido como entregado. Solo aplica a pedidos CONFIRMADOS. */
    public void marcarEntregado() {
        if (estado != EstadoPedido.CONFIRMADO) {
            throw new IllegalStateException("Solo un pedido CONFIRMADO puede entregarse (estado actual: " + estado + ")");
        }
        estado = EstadoPedido.ENTREGADO;
    }

    /** Cancela el pedido. Un pedido entregado o ya cancelado no puede cancelarse. */
    public void cancelar() {
        if (estado == EstadoPedido.ENTREGADO || estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("El pedido " + codigo + " no puede cancelarse (estado: " + estado + ")");
        }
        estado = EstadoPedido.CANCELADO;
    }

    /** @return true si el pedido cuenta como ingreso (CONFIRMADO o ENTREGADO) */
    public boolean cuentaComoIngreso() {
        return estado == EstadoPedido.CONFIRMADO || estado == EstadoPedido.ENTREGADO;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public Cliente getTheCliente() { return theCliente; }
    public void setTheCliente(Cliente theCliente) { this.theCliente = theCliente; }
    public ModalidadEntrega getModalidad() { return modalidad; }
    public void setModalidad(ModalidadEntrega modalidad) { this.modalidad = modalidad; }
    public EstadoPedido getEstado() { return estado; }
    public List<DetallePedido> getListPedidoDetalles() { return listPedidoDetalles; }
    public Domicilio getTheDomicilio() { return theDomicilio; }

    @Override
    public String toString() {
        return "Pedido " + codigo + " | Fecha: " + fecha + " | Estado: " + estado
                + " | Total: $" + String.format("%,.0f", calcularValorTotal());
    }
}
