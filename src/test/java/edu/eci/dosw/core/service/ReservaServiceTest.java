package edu.eci.dosw.core.service;

import edu.eci.dosw.core.model.Reserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link ReservaService} del paquete core,
 * verificando los escenarios exitosos y de error estipulados en el diseño.
 */
class ReservaServiceTest {

    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        reservaService = new ReservaService();
    }

    // --- ESCENARIOS EXITOSOS ---

    /**
     * Dado que tengo 1 reserva registrada,
     * Cuando lo consulto a nivel de servicio,
     * entonces la consulta será exitosa validando el campo id.
     */
    @Test
    void consultarReserva_DebeRetornarReservaExistente_YValidarCampoId() {
        // Arrange: crear y registrar una reserva
        Reserva reserva = new Reserva(null, "user123", "book456");
        Reserva reservaCreada = reservaService.crearReserva(reserva);

        // Act: consultar la reserva por su ID
        Optional<Reserva> resultado = reservaService.obtenerReservaPorId(reservaCreada.getId());

        // Assert: validar que se encontró la reserva y tiene el ID correcto
        assertTrue(resultado.isPresent());
        assertNotNull(resultado.get().getId());
        assertEquals(reservaCreada.getId(), resultado.get().getId());
    }

    /**
     * Dado que no hay ninguna reserva registrada,
     * Cuándo la consultó a nivel de servicio,
     * Entonces la consulta no retorna ningún resultado.
     */
    @Test
    void consultarReserva_DebeRetornarVacio_CuandoNoExistenReservas() {
        // Arrange: no crear ninguna reserva

        // Act: intentar consultar un ID que no existe
        Optional<Reserva> resultado = reservaService.obtenerReservaPorId("idNoExistente");

        // Assert: validar que no se encontró resultado
        assertFalse(resultado.isPresent());
    }

    /**
     * Dado que no hay ninguna reserva registrada,
     * Cuándo lo creo a nivel de servicio,
     * entonces la creación será exitosa.
     */
    @Test
    void crearReserva_DebeRegistrarseExitosamente_CuandoNoHayReservas() {
        // Arrange: preparar una nueva reserva
        Reserva reserva = new Reserva(null, "usuario789", "libro321");

        // Act: crear la reserva
        Reserva resultado = reservaService.crearReserva(reserva);

        // Assert: validar que la reserva fue creada con éxito
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("usuario789", resultado.getUserId());
        assertEquals("libro321", resultado.getBookId());
        assertEquals("PENDIENTE", resultado.getStatus());
        assertEquals(1, reservaService.contarReservas());
    }

    /**
     * Dado que tengo 1 reserva registrada,
     * Cuándo la elimino a nivel de servicio,
     * entonces la eliminación será exitosa.
     */
    @Test
    void eliminarReserva_DebeEliminarse_CuandoExisteUnaReserva() {
        // Arrange: crear y registrar una reserva
        Reserva reserva = new Reserva(null, "usuario111", "libro222");
        Reserva reservaCreada = reservaService.crearReserva(reserva);

        // Act: eliminar la reserva
        boolean eliminada = reservaService.eliminarReserva(reservaCreada.getId());

        // Assert: validar que la eliminación fue exitosa
        assertTrue(eliminada);
        assertEquals(0, reservaService.contarReservas());
    }

    /**
     * Dado que tengo 1 reserva registrada,
     * Cuándo la elimino y consulto a nivel de servicio,
     * entonces el resultado de la consulta no retorna ningún resultado.
     */
    @Test
    void eliminarYConsultarReserva_DebeDevolverVacio_DespuesDeEliminar() {
        // Arrange: crear y registrar una reserva
        Reserva reserva = new Reserva(null, "usuario333", "libro444");
        Reserva reservaCreada = reservaService.crearReserva(reserva);

        // Act: eliminar la reserva
        reservaService.eliminarReserva(reservaCreada.getId());

        // Act: intentar consultar la reserva eliminada
        Optional<Reserva> resultado = reservaService.obtenerReservaPorId(reservaCreada.getId());

        // Assert: validar que la consulta no retorna resultado
        assertFalse(resultado.isPresent());
    }

    // --- ESCENARIOS ADICIONALES ---

    @Test
    void obtenerTodasLasReservas_DebeRetornarTodasLasReservasRegistradas() {
        // Arrange: crear múltiples reservas
        reservaService.crearReserva(new Reserva(null, "user1", "book1"));
        reservaService.crearReserva(new Reserva(null, "user2", "book2"));
        reservaService.crearReserva(new Reserva(null, "user3", "book3"));

        // Act: obtener todas las reservas
        List<Reserva> todas = reservaService.obtenerTodasLasReservas();

        // Assert: validar que se retornaron todas
        assertEquals(3, todas.size());
    }

    @Test
    void obtenerReservasPorUsuario_DebeRetornarSoloReservasDelUsuario() {
        // Arrange: crear reservas de múltiples usuarios
        reservaService.crearReserva(new Reserva(null, "usuario1", "libro1"));
        reservaService.crearReserva(new Reserva(null, "usuario1", "libro2"));
        reservaService.crearReserva(new Reserva(null, "usuario2", "libro3"));

        // Act: obtener reservas del usuario1
        List<Reserva> reservasUsuario = reservaService.obtenerReservasPorUsuario("usuario1");

        // Assert: validar cantidad de resultados
        assertEquals(2, reservasUsuario.size());
        assertTrue(reservasUsuario.stream().allMatch(r -> r.getUserId().equals("usuario1")));
    }

    @Test
    void obtenerReservasPorLibro_DebeRetornarSoloReservasDelLibro() {
        // Arrange: crear reservas para múltiples libros
        reservaService.crearReserva(new Reserva(null, "user1", "libro1"));
        reservaService.crearReserva(new Reserva(null, "user2", "libro1"));
        reservaService.crearReserva(new Reserva(null, "user3", "libro2"));

        // Act: obtener reservas del libro1
        List<Reserva> reservasLibro = reservaService.obtenerReservasPorLibro("libro1");

        // Assert: validar cantidad de resultados
        assertEquals(2, reservasLibro.size());
        assertTrue(reservasLibro.stream().allMatch(r -> r.getBookId().equals("libro1")));
    }

    @Test
    void actualizarEstadoReserva_DebeActualizarElEstadoCorrectamente() {
        // Arrange: crear una reserva
        Reserva reserva = reservaService.crearReserva(new Reserva(null, "user1", "book1"));

        // Act: actualizar el estado
        reservaService.actualizarEstadoReserva(reserva.getId(), "CONFIRMADA");

        // Assert: validar que el estado fue actualizado
        Optional<Reserva> reservaActualizada = reservaService.obtenerReservaPorId(reserva.getId());
        assertTrue(reservaActualizada.isPresent());
        assertEquals("CONFIRMADA", reservaActualizada.get().getStatus());
    }

    @Test
    void contarReservas_DebeRetornarCantidadExactaDeReservas() {
        // Arrange: crear múltiples reservas
        reservaService.crearReserva(new Reserva(null, "user1", "book1"));
        reservaService.crearReserva(new Reserva(null, "user2", "book2"));

        // Act: contar reservas
        int cantidad = reservaService.contarReservas();

        // Assert: validar cantidad
        assertEquals(2, cantidad);
    }

    // --- ESCENARIOS DE ERROR ---

    @Test
    void crearReserva_DebeLanzarExcepcion_CuandoReservaEsNula() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(null);
        });
    }

    @Test
    void crearReserva_DebeLanzarExcepcion_CuandoUserIdEstaVacio() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(new Reserva(null, "", "book1"));
        });
    }

    @Test
    void crearReserva_DebeLanzarExcepcion_CuandoBookIdEstaVacio() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(new Reserva(null, "user1", ""));
        });
    }

    @Test
    void actualizarEstadoReserva_DebeLanzarExcepcion_CuandoReservaNoExiste() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            reservaService.actualizarEstadoReserva("idNoExistente", "CANCELADA");
        });
    }

    @Test
    void limpiarTodasLasReservas_DebeBorrarTodas() {
        // Arrange: crear múltiples reservas
        reservaService.crearReserva(new Reserva(null, "user1", "book1"));
        reservaService.crearReserva(new Reserva(null, "user2", "book2"));

        // Act: limpiar todas
        reservaService.limpiarTodasLasReservas();

        // Assert: validar que están todas borradas
        assertEquals(0, reservaService.contarReservas());
    }
}
