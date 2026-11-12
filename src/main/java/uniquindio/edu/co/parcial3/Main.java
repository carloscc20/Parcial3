package uniquindio.edu.co.parcial3;

import uniquindio.edu.co.parcial3.model.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/** Menú interactivo (JOptionPane) del restaurante El Buen Comer. */
public class Main {

    private static final String TITULO = "El Buen Comer";
    private static Restaurante restaurante;

    public static void main(String[] args) {
        restaurante = new Restaurante("El Buen Comer", "900.123.456-7", "Armenia, Quindío");
        inicializarDatos();

        String[] opciones = {
                "1. Registrar producto",
                "2. Registrar cliente",
                "3. Registrar y confirmar pedido",
                "4. Calcular valor total de un pedido",
                "5. Historial de pedidos de un cliente",
                "6. Actualizar estado de un pedido",
                "7. Consultar productos del menú",
                "8. Consultar ventas del restaurante",
                "0. Salir"
        };
        boolean salir = false;
        while (!salir) {
            String opcion = (String) JOptionPane.showInputDialog(null, "Seleccione una opción:",
                    TITULO + " - Menú principal", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
            if (opcion == null || opcion.startsWith("0")) {
                salir = true;
                continue;
            }
            try {
                switch (opcion.charAt(0)) {
                    case '1' -> registrarProducto();
                    case '2' -> registrarCliente();
                    case '3' -> registrarPedido();
                    case '4' -> calcularTotalPedido();
                    case '5' -> consultarHistorial();
                    case '6' -> actualizarEstadoPedido();
                    case '7' -> consultarProductos();
                    case '8' -> consultarVentas();
                }
            } catch (CanceladoException e) {
                // el usuario cerró una ventana: se regresa al menú
            } catch (IllegalArgumentException | IllegalStateException e) {
                error(e.getMessage());
            }
        }
        JOptionPane.showMessageDialog(null, "¡Gracias por usar el sistema!", TITULO, JOptionPane.INFORMATION_MESSAGE);
    }

    // ===================== DATOS DE EJEMPLO =====================

    private static void inicializarDatos() {
        restaurante.registrarProducto(new PlatoFuerte("P1", "Bandeja paisa", "Plato típico", 20000, 30, 400, TipoPreparacion.ASADO, true));
        restaurante.registrarProducto(new PlatoFuerte("P2", "Pechuga a la plancha", "Con ensalada", 18000, 25, 350, TipoPreparacion.A_LA_PLANCHA, true));
        restaurante.registrarProducto(new PlatoFuerte("P3", "Mini hamburguesa", "Porción pequeña", 12000, 15, 150, TipoPreparacion.FRITO, true));
        restaurante.registrarProducto(new Bebida("B1", "Limonada", "Limonada natural", 5000, 5, 400, 5, TipoTemperatura.FRIA, false));
        restaurante.registrarProducto(new Bebida("B2", "Café especial", "Café de origen", 6000, 8, 250, 70, TipoTemperatura.CALIENTE, true));
        restaurante.registrarProducto(new Postre("D1", "Cheesecake", "De frutos rojos", 8000, 10, 150, 4, true));
        restaurante.registrarProducto(new Postre("D2", "Brownie", "Con nueces", 6000, 10, 120, 20, false));

        restaurante.registrarCliente(new Cliente("1094", "Carlos Castaño", "3001234567", "carlos@correo.com", "Calle 10 # 5-20"));
        restaurante.registrarCliente(new Cliente("2020", "Ana Gómez", "3109876543", "ana@correo.com", "Carrera 14 # 20-30"));

        restaurante.registrarEmpresaDomicilio(new RapidoExpress());
        restaurante.registrarEmpresaDomicilio(new EntregaSegura());

        Pedido pedido1 = new Pedido("PED1", LocalDate.now(), restaurante.buscarCliente("1094"), ModalidadEntrega.CONSUMO_RESTAURANTE);
        pedido1.agregarProducto(restaurante.buscarProducto("P1"), 2);
        pedido1.agregarProducto(restaurante.buscarProducto("B1"), 1);
        restaurante.registrarPedido(pedido1);
    }

    // ===================== OPCIONES DEL MENÚ =====================

    private static void registrarProducto() {
        String[] tipos = {"Plato fuerte", "Bebida", "Postre"};
        String tipo = seleccionar("Tipo de producto:", tipos);
        String codigo = leerTexto("Código:");
        String nombre = leerTexto("Nombre:");
        String descripcion = leerTexto("Descripción:");
        double precioBase = leerDouble("Precio base:");
        int tiempo = leerEntero("Tiempo de preparación (min):");

        Producto producto;
        if (tipo.equals("Plato fuerte")) {
            double peso = leerDouble("Peso en gramos:");
            TipoPreparacion preparacion = seleccionar("Tipo de preparación:", TipoPreparacion.values());
            boolean coccion = confirmar("¿Superó el control de cocción?");
            producto = new PlatoFuerte(codigo, nombre, descripcion, precioBase, tiempo, peso, preparacion, coccion);
        } else if (tipo.equals("Bebida")) {
            double volumen = leerDouble("Volumen en ml:");
            TipoTemperatura tipoTemp = seleccionar("¿Fría o caliente?", TipoTemperatura.values());
            double temperatura = leerDouble("Temperatura de servicio (°C):");
            boolean especial = confirmar("¿Requiere preparación especial?");
            producto = new Bebida(codigo, nombre, descripcion, precioBase, tiempo, volumen, temperatura, tipoTemp, especial);
        } else {
            double peso = leerDouble("Peso en gramos:");
            boolean refrigeracion = confirmar("¿Requiere refrigeración?");
            double temperatura = leerDouble("Temperatura de almacenamiento (°C):");
            producto = new Postre(codigo, nombre, descripcion, precioBase, tiempo, peso, temperatura, refrigeracion);
        }
        restaurante.registrarProducto(producto);
        info("Producto registrado:\n" + producto);
    }

    private static void registrarCliente() {
        Cliente cliente = new Cliente(
                leerTexto("Documento:"),
                leerTexto("Nombre completo:"),
                leerTexto("Teléfono:"),
                leerTexto("Correo electrónico:"),
                leerTexto("Dirección de residencia:"));
        restaurante.registrarCliente(cliente);
        info("Cliente registrado:\n" + cliente);
    }

    private static void registrarPedido() {
        String codigo = leerTexto("Código del pedido:");
        Cliente cliente = restaurante.buscarCliente(leerTexto("Documento del cliente:"));
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no está registrado");
        }
        ModalidadEntrega modalidad = seleccionar("Modalidad de entrega:", ModalidadEntrega.values());
        Pedido pedido = new Pedido(codigo, LocalDate.now(), cliente, modalidad);

        Producto[] productos = restaurante.getListRestauranteProductos().toArray(new Producto[0]);
        do {
            Producto producto = seleccionar("Seleccione un producto:", productos);
            int cantidad = leerEntero("Cantidad de " + producto.getNombre() + ":");
            pedido.agregarProducto(producto, cantidad);
        } while (confirmar("¿Desea agregar otro producto?"));

        if (pedido.esADomicilio()) {
            IEmpresaDomicilio[] empresas = restaurante.getListRestauranteEmpresas().toArray(new IEmpresaDomicilio[0]);
            IEmpresaDomicilio empresa = seleccionar("Empresa de domicilio:", empresas);
            double distancia = leerDouble("Distancia del recorrido (km):");
            pedido.asignarDomicilio(empresa, distancia);
        }
        restaurante.registrarPedido(pedido);
        info("Pedido CONFIRMADO\n\n" + detallePedido(pedido));
    }

    private static void calcularTotalPedido() {
        Pedido pedido = restaurante.buscarPedido(leerTexto("Código del pedido:"));
        if (pedido == null) {
            throw new IllegalArgumentException("No existe un pedido con ese código");
        }
        info(detallePedido(pedido));
    }

    private static void consultarHistorial() {
        String documento = leerTexto("Documento del cliente:");
        List<Pedido> historial = restaurante.consultarHistorialCliente(documento);
        StringBuilder sb = new StringBuilder("Historial de " + restaurante.buscarCliente(documento).getNombreCompleto() + "\n\n");
        if (historial.isEmpty()) {
            sb.append("El cliente no tiene pedidos.");
        }
        for (Pedido pedido : historial) {
            sb.append(pedido).append("\n");
        }
        info(sb.toString());
    }

    private static void actualizarEstadoPedido() {
        String codigo = leerTexto("Código del pedido:");
        String[] acciones = {"Marcar como ENTREGADO", "CANCELAR pedido"};
        String accion = seleccionar("Acción:", acciones);
        if (accion.equals(acciones[0])) {
            restaurante.marcarPedidoEntregado(codigo);
        } else {
            restaurante.cancelarPedido(codigo);
        }
        info("Pedido actualizado:\n" + restaurante.buscarPedido(codigo));
    }

    private static void consultarProductos() {
        StringBuilder sb = new StringBuilder("PRODUCTOS DISPONIBLES (cumplen calidad)\n");
        for (Producto producto : restaurante.obtenerProductosDisponibles()) {
            sb.append("  ").append(producto).append("\n");
        }
        sb.append("\nPRODUCTOS QUE NO CUMPLEN CALIDAD\n");
        for (Producto producto : restaurante.obtenerProductosNoAptos()) {
            sb.append("  ").append(producto).append("\n");
        }
        info(sb.toString());
    }

    private static void consultarVentas() {
        Producto masVendido = restaurante.obtenerProductoMasVendido();
        CategoriaProducto categoria = restaurante.obtenerCategoriaMayorIngreso();
        String sb = "VENTAS - " + restaurante + "\n\n"
                + "Ingresos propios del restaurante: $" + dinero(restaurante.calcularIngresosRestaurante()) + "\n"
                + "Valores por domicilios:          $" + dinero(restaurante.calcularIngresosDomicilios()) + "\n\n"
                + "Producto más vendido: " + (masVendido == null ? "Sin ventas" : masVendido.getNombre()) + "\n"
                + "Categoría con mayores ingresos: " + (categoria == null ? "Sin ventas" : categoria) + "\n\n"
                + "Ingresos por categoría:\n"
                + "  PLATO_FUERTE: $" + dinero(restaurante.calcularIngresosCategoria(CategoriaProducto.PLATO_FUERTE)) + "\n"
                + "  BEBIDA:       $" + dinero(restaurante.calcularIngresosCategoria(CategoriaProducto.BEBIDA)) + "\n"
                + "  POSTRE:       $" + dinero(restaurante.calcularIngresosCategoria(CategoriaProducto.POSTRE)) + "\n"
                + "\n(Los pedidos cancelados no se cuentan como ingresos)";
        info(sb);
    }

    // ===================== AYUDANTES =====================

    private static String detallePedido(Pedido pedido) {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido: ").append(pedido.getCodigo()).append("   Fecha: ").append(pedido.getFecha()).append("\n");
        sb.append("Cliente: ").append(pedido.getTheCliente().getNombreCompleto()).append("\n");
        sb.append("Modalidad: ").append(pedido.getModalidad()).append("   Estado: ").append(pedido.getEstado()).append("\n\n");
        for (DetallePedido detalle : pedido.getListPedidoDetalles()) {
            sb.append(String.format("%-22s x%-3d $%s%n", detalle.producto().getNombre(), detalle.cantidad(), dinero(detalle.calcularSubtotal())));
        }
        sb.append("\nValor productos: $").append(dinero(pedido.calcularValorProductos())).append("\n");
        if (pedido.getTheDomicilio() != null) {
            sb.append("Domicilio (").append(pedido.getTheDomicilio().empresa().getNombre()).append(", ")
                    .append(pedido.getTheDomicilio().distanciaKm()).append(" km): $")
                    .append(dinero(pedido.calcularCostoDomicilio())).append("\n");
        }
        sb.append("VALOR TOTAL: $").append(dinero(pedido.calcularValorTotal()));
        return sb.toString();
    }

    private static String dinero(double valor) {
        return String.format("%,.0f", valor);
    }

    /** Muestra texto en un área con letra monoespaciada para que las columnas queden alineadas. */
    private static void info(String mensaje) {
        JTextArea area = new JTextArea(mensaje);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JOptionPane.showMessageDialog(null, new JScrollPane(area), TITULO, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void error(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO + " - Error", JOptionPane.ERROR_MESSAGE);
    }

    private static String leerTexto(String mensaje) {
        String texto = JOptionPane.showInputDialog(null, mensaje, TITULO, JOptionPane.QUESTION_MESSAGE);
        if (texto == null) {
            throw new CanceladoException();
        }
        if (texto.isBlank()) {
            throw new IllegalArgumentException("El campo no puede estar vacío");
        }
        return texto.trim();
    }

    private static double leerDouble(String mensaje) {
        try {
            return Double.parseDouble(leerTexto(mensaje).replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Debe ingresar un número válido");
        }
    }

    private static int leerEntero(String mensaje) {
        try {
            return Integer.parseInt(leerTexto(mensaje));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Debe ingresar un número entero válido");
        }
    }

    private static <T> T seleccionar(String mensaje, T[] opciones) {
        Object elegido = JOptionPane.showInputDialog(null, mensaje, TITULO,
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (elegido == null) {
            throw new CanceladoException();
        }
        @SuppressWarnings("unchecked")
        T resultado = (T) elegido;
        return resultado;
    }

    private static boolean confirmar(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(null, mensaje, TITULO, JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.CLOSED_OPTION) {
            throw new CanceladoException();
        }
        return respuesta == JOptionPane.YES_OPTION;
    }

    /** Se lanza cuando el usuario cierra o cancela una ventana. */
    private static class CanceladoException extends RuntimeException {
    }
}
