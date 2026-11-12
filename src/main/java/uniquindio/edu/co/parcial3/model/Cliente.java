package uniquindio.edu.co.parcial3.model;

import java.util.ArrayList;
import java.util.List;

/** Cliente del restaurante con su historial de pedidos. */
public class Cliente {
    private String documento;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String direccion;
    private List<Pedido> listClientePedidos;

    /**
     * Constructor de la clase Cliente.
     * @param documento número de documento
     * @param nombreCompleto nombre completo
     * @param telefono teléfono
     * @param correo correo electrónico
     * @param direccion dirección de residencia
     */
    public Cliente(String documento, String nombreCompleto, String telefono, String correo, String direccion) {
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.listClientePedidos = new ArrayList<>();
    }

    /**
     * Agrega un pedido al historial del cliente evitando repetidos.
     * @param pedido pedido confirmado
     */
    public void agregarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        for (Pedido p : listClientePedidos) {
            if (p.getCodigo().equals(pedido.getCodigo())) {
                throw new IllegalArgumentException("El pedido " + pedido.getCodigo() + " ya está en el historial");
            }
        }
        listClientePedidos.add(pedido);
    }

    /** @return historial de pedidos del cliente */
    public List<Pedido> obtenerHistorial() {
        return listClientePedidos;
    }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public List<Pedido> getListClientePedidos() { return listClientePedidos; }

    @Override
    public String toString() {
        return documento + " - " + nombreCompleto + " | Tel: " + telefono + " | " + correo;
    }
}
