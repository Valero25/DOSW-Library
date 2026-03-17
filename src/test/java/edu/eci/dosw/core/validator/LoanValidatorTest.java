package edu.eci.dosw.core.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link LoanValidator}.
 */
class LoanValidatorTest {

    @Test
    void validarIds_DebePasar_CuandoAmbasIdssonValidas() {
        assertDoesNotThrow(() -> LoanValidator.validateIds("libro-123", "usuario-456"));
    }

    @Test
    void validarIds_DebeLanzarExcepcion_CuandoIdDeLibroEstaVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> LoanValidator.validateIds("", "usuario-456"));
    }

    @Test
    void validarIds_DebeLanzarExcepcion_CuandoIdDeUsuarioEstaVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> LoanValidator.validateIds("libro-123", " "));
    }

    @Test
    void validarIds_DebeLanzarExcepcion_CuandoIdDeLibroEsNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> LoanValidator.validateIds(null, "usuario-456"));
    }
}
