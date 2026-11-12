package uniquindio.edu.co.parcial3.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas unitarias (TDD) de las principales funcionalidades de la clase Restaurante. */
class RestauranteTest {

    private Restaurante restaurante;
    private PlatoFuerte plato;
    private Bebida bebida;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante("El Buen Comer", "900123456", "Armenia");
        plato = new PlatoFuerte("P1", "Bandeja paisa", "Plato típico", 20000, 30, 400, TipoPreparacion.ASADO, true);
        bebida = new Bebida("B1", "Limonada", "Natural", 5000, 5, 400, 5, TipoTemperatura.FRIA, false);
        cliente = new Cliente("1094", "Carlos", "3001234567", "carlos@correo.com", "Calle 10");
    }

    /** Prueba 1: no se permite registrar dos productos con el mismo código. */
    @Test
    void rechazarProductoConCodigoDuplicado() {
        restaurante.registrarProducto(plato);
        assertThrows(IllegalArgumentException.class, () -> restaurante.registrarProducto(plato));
    }

    /** Prueba 2: no se permite registrar dos clientes con el mismo documento. */
    @Test
    void rechazarClienteConDocumentoDuplicado() {
        restaurante.registrarCliente(cliente);
        assertThrows(IllegalArgumentException.class, () -> restaurante.registrarCliente(cliente));
    }

    /** Prueba 3: un pedido de un cliente no registrado no puede confirmarse. */
    @Test
    void rechazarPedidoDeClienteNoRegistrado() {
        restaurante.registrarProducto(plato);
        Pedido pedido = new Pedido("PED1", LocalDate.now(), cliente, ModalidadEntrega.CONSUMO_RESTAURANTE);
        pedido.agregarProducto(plato, 2);
        assertThrows(IllegalArgumentException.class, () -> restaurante.registrarPedido(pedido));
    }

    /** Prueba 4: un producto que no cumple calidad (plato de 150 g) no puede incluirse en un pedido confirmado. */
    @Test
    void rechazarProductoQueNoCumpleCalidad() {
        PlatoFuerte platoPequeno = new PlatoFuerte("P3", "Mini plato", "Porción", 12000, 15, 150, TipoPreparacion.FRITO, true);
        restaurante.registrarProducto(platoPequeno);
        restaurante.registrarCliente(cliente);
        Pedido pedido = new Pedido("PED1", LocalDate.now(), cliente, ModalidadEntrega.CONSUMO_RESTAURANTE);
        pedido.agregarProducto(platoPequeno, 1);
        assertThrows(IllegalArgumentException.class, () -> restaurante.registrarPedido(pedido));
    }

    /** Prueba 5: un pedido válido queda CONFIRMADO, en el historial del cliente y con el total correcto. */
    @Test
    void registrarYConfirmarPedidoValido() {
        restaurante.registrarProducto(plato);
        restaurante.registrarProducto(bebida);
        restaurante.registrarCliente(cliente);
        Pedido pedido = new Pedido("PED1", LocalDate.now(), cliente, ModalidadEntrega.CONSUMO_RESTAURANTE);
        pedido.agregarProducto(plato, 2);
        pedido.agregarProducto(bebida, 1);

        restaurante.registrarPedido(pedido);

        assertEquals(EstadoPedido.CONFIRMADO, pedido.getEstado());
        assertEquals(1, restaurante.consultarHistorialCliente("1094").size());
        assertEquals(53500, restaurante.calcularTotalPedido("PED1"), 0.001);
    }
}
