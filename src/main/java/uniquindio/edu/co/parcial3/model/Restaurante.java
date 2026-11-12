package uniquindio.edu.co.parcial3.model;

import java.util.ArrayList;
import java.util.List;

/** Clase principal del sistema: administra menú, clientes, pedidos, domicilios y ventas. */
public class Restaurante {
    private String nombreComercial;
    private String nit;
    private String direccion;
    private List<Producto> listRestauranteProductos;
    private List<Cliente> listRestauranteClientes;
    private List<Pedido> listRestaurantePedidos;
    private List<IEmpresaDomicilio> listRestauranteEmpresas;

    /**
     * Constructor de la clase Restaurante.
     * @param nombreComercial nombre comercial
     * @param nit NIT del restaurante
     * @param direccion dirección del restaurante
     */
    public Restaurante(String nombreComercial, String nit, String direccion) {
        this.nombreComercial = nombreComercial;
        this.nit = nit;
        this.direccion = direccion;
        this.listRestauranteProductos = new ArrayList<>();
        this.listRestauranteClientes = new ArrayList<>();
        this.listRestaurantePedidos = new ArrayList<>();
        this.listRestauranteEmpresas = new ArrayList<>();
    }

    // ===================== PRODUCTOS =====================

    /** Funcionalidad 1: registra un producto verificando que su código no esté duplicado. */
    public void registrarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (buscarProducto(producto.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con el código " + producto.getCodigo());
        }
        listRestauranteProductos.add(producto);
    }

    /** @return el producto con ese código o null si no existe */
    public Producto buscarProducto(String codigo) {
        for (Producto producto : listRestauranteProductos) {
            if (producto.getCodigo().equalsIgnoreCase(codigo)) {
                return producto;
            }
        }
        return null;
    }

    /** @return productos que cumplen las condiciones de calidad */
    public List<Producto> obtenerProductosDisponibles() {
        List<Producto> disponibles = new ArrayList<>();
        for (Producto producto : listRestauranteProductos) {
            if (producto.cumpleCalidad()) {
                disponibles.add(producto);
            }
        }
        return disponibles;
    }

    /** @return productos que NO cumplen las condiciones de calidad */
    public List<Producto> obtenerProductosNoAptos() {
        List<Producto> noAptos = new ArrayList<>();
        for (Producto producto : listRestauranteProductos) {
            if (!producto.cumpleCalidad()) {
                noAptos.add(producto);
            }
        }
        return noAptos;
    }

    // ===================== CLIENTES =====================

    /** Funcionalidad 2: registra un cliente verificando que su documento no esté duplicado. */
    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        if (buscarCliente(cliente.getDocumento()) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con el documento " + cliente.getDocumento());
        }
        listRestauranteClientes.add(cliente);
    }

    /** @return el cliente con ese documento o null si no existe */
    public Cliente buscarCliente(String documento) {
        for (Cliente cliente : listRestauranteClientes) {
            if (cliente.getDocumento().equals(documento)) {
                return cliente;
            }
        }
        return null;
    }

    // ===================== DOMICILIOS =====================

    /** Registra una empresa externa de domicilio. */
    public void registrarEmpresaDomicilio(IEmpresaDomicilio empresa) {
        if (empresa == null) {
            throw new IllegalArgumentException("La empresa no puede ser nula");
        }
        listRestauranteEmpresas.add(empresa);
    }

    // ===================== PEDIDOS =====================

    /**
     * Funcionalidad 3: registra y confirma un pedido.
     * Verifica: código no duplicado, cliente registrado, productos del menú,
     * cantidades mayores que cero, calidad y, si es a domicilio, empresa que aceptó.
     */
    public void registrarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        if (buscarPedido(pedido.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un pedido con el código " + pedido.getCodigo());
        }
        Cliente cliente = pedido.getTheCliente();
        if (cliente == null || buscarCliente(cliente.getDocumento()) == null) {
            throw new IllegalArgumentException("El cliente no está registrado");
        }
        if (pedido.getListPedidoDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido no tiene productos");
        }
        for (DetallePedido detalle : pedido.getListPedidoDetalles()) {
            Producto producto = detalle.producto();
            if (buscarProducto(producto.getCodigo()) == null) {
                throw new IllegalArgumentException("El producto " + producto.getCodigo() + " no pertenece al menú");
            }
            if (detalle.cantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad de " + producto.getNombre() + " debe ser mayor que cero");
            }
            if (!producto.cumpleCalidad()) {
                throw new IllegalArgumentException("El producto " + producto.getNombre() + " no cumple las condiciones de calidad");
            }
        }
        if (pedido.esADomicilio() && pedido.getTheDomicilio() == null) {
            throw new IllegalArgumentException("Ninguna empresa externa ha aceptado el domicilio");
        }
        pedido.confirmar();
        listRestaurantePedidos.add(pedido);
        cliente.agregarPedido(pedido);
    }

    /** @return el pedido con ese código o null si no existe */
    public Pedido buscarPedido(String codigo) {
        for (Pedido pedido : listRestaurantePedidos) {
            if (pedido.getCodigo().equalsIgnoreCase(codigo)) {
                return pedido;
            }
        }
        return null;
    }

    /** Funcionalidad 4: calcula el valor total de un pedido consultado por su código. */
    public double calcularTotalPedido(String codigoPedido) {
        Pedido pedido = buscarPedido(codigoPedido);
        if (pedido == null) {
            throw new IllegalArgumentException("No existe el pedido " + codigoPedido);
        }
        return pedido.calcularValorTotal();
    }

    /** Funcionalidad 5: historial de pedidos de un cliente buscado por su documento. */
    public List<Pedido> consultarHistorialCliente(String documento) {
        Cliente cliente = buscarCliente(documento);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con el documento " + documento);
        }
        return cliente.obtenerHistorial();
    }

    /** Marca como entregado un pedido confirmado. */
    public void marcarPedidoEntregado(String codigoPedido) {
        Pedido pedido = buscarPedido(codigoPedido);
        if (pedido == null) {
            throw new IllegalArgumentException("No existe el pedido " + codigoPedido);
        }
        pedido.marcarEntregado();
    }

    /** Cancela un pedido (deja de contar como ingreso). */
    public void cancelarPedido(String codigoPedido) {
        Pedido pedido = buscarPedido(codigoPedido);
        if (pedido == null) {
            throw new IllegalArgumentException("No existe el pedido " + codigoPedido);
        }
        pedido.cancelar();
    }

    // ===================== VENTAS =====================

    /** @return ingresos propios del restaurante (productos), sin domicilios ni pedidos cancelados */
    public double calcularIngresosRestaurante() {
        double total = 0;
        for (Pedido pedido : listRestaurantePedidos) {
            if (pedido.cuentaComoIngreso()) {
                total += pedido.calcularValorProductos();
            }
        }
        return total;
    }

    /** @return valores correspondientes a domicilios, sin pedidos cancelados */
    public double calcularIngresosDomicilios() {
        double total = 0;
        for (Pedido pedido : listRestaurantePedidos) {
            if (pedido.cuentaComoIngreso()) {
                total += pedido.calcularCostoDomicilio();
            }
        }
        return total;
    }

    /** @return producto con mayor cantidad de unidades vendidas, o null si no hay ventas */
    public Producto obtenerProductoMasVendido() {
        Producto masVendido = null;
        int maxUnidades = 0;
        for (Producto producto : listRestauranteProductos) {
            int unidades = 0;
            for (Pedido pedido : listRestaurantePedidos) {
                if (pedido.cuentaComoIngreso()) {
                    for (DetallePedido detalle : pedido.getListPedidoDetalles()) {
                        if (detalle.producto().getCodigo().equals(producto.getCodigo())) {
                            unidades += detalle.cantidad();
                        }
                    }
                }
            }
            if (unidades > maxUnidades) {
                maxUnidades = unidades;
                masVendido = producto;
            }
        }
        return masVendido;
    }

    /** @return categoría que genera mayores ingresos, o null si no hay ventas */
    public CategoriaProducto obtenerCategoriaMayorIngreso() {
        CategoriaProducto mayor = null;
        double maxIngreso = 0;
        for (CategoriaProducto categoria : CategoriaProducto.values()) {
            double ingreso = calcularIngresosCategoria(categoria);
            if (ingreso > maxIngreso) {
                maxIngreso = ingreso;
                mayor = categoria;
            }
        }
        return mayor;
    }

    /** @return ingresos generados por una categoría (sin pedidos cancelados) */
    public double calcularIngresosCategoria(CategoriaProducto categoria) {
        double total = 0;
        for (Pedido pedido : listRestaurantePedidos) {
            if (pedido.cuentaComoIngreso()) {
                for (DetallePedido detalle : pedido.getListPedidoDetalles()) {
                    if (detalle.producto().obtenerCategoria() == categoria) {
                        total += detalle.calcularSubtotal();
                    }
                }
            }
        }
        return total;
    }

    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public List<Producto> getListRestauranteProductos() { return listRestauranteProductos; }
    public List<Cliente> getListRestauranteClientes() { return listRestauranteClientes; }
    public List<Pedido> getListRestaurantePedidos() { return listRestaurantePedidos; }
    public List<IEmpresaDomicilio> getListRestauranteEmpresas() { return listRestauranteEmpresas; }

    @Override
    public String toString() {
        return nombreComercial + " | NIT: " + nit + " | " + direccion;
    }
}
