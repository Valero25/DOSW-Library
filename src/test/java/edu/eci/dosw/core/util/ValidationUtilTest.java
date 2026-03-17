package edu.eci.dosw.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link ValidationUtil} del paquete core.
 */
class ValidationUtilTest {

    @Test
    void validarNoVacio_DebePasar_CuandoValorEsValido() {
        assertDoesNotThrow(() -> ValidationUtil.validateNotBlank("hola", "campo"));
    }

    @Test
    void validarNoVacio_DebeLanzarExcepcion_CuandoValorEsNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateNotBlank(null, "campo"));
    }

    @Test
    void validarNoVacio_DebeLanzarExcepcion_CuandoValorEstaVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateNotBlank("  ", "campo"));
    }

    @Test
    void validarNoNulo_DebePasar_CuandoValorNoEsNulo() {
        assertDoesNotThrow(() -> ValidationUtil.validateNotNull(new Object(), "objeto"));
    }

    @Test
    void validarNoNulo_DebeLanzarExcepcion_CuandoValorEsNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateNotNull(null, "objeto"));
    }

    @Test
    void validarIsbn_DebeRetornarVerdadero_ParaIsbnDe13Digitos() {
        assertTrue(ValidationUtil.isValidIsbn("9780132350884"));
    }

    @Test
    void validarIsbn_DebeRetornarVerdadero_ParaIsbnConGuiones() {
        assertTrue(ValidationUtil.isValidIsbn("978-0-13-235088-4"));
    }

    @Test
    void validarIsbn_DebeRetornarFalso_ParaIsbnCorto() {
        assertFalse(ValidationUtil.isValidIsbn("12345"));
    }

    @Test
    void validarIsbn_DebeRetornarFalso_ParaNulo() {
        assertFalse(ValidationUtil.isValidIsbn(null));
    }

    @Test
    void validarCorreo_DebeRetornarVerdadero_ParaCorreoValido() {
        assertTrue(ValidationUtil.isValidEmail("usuario@eci.edu.co"));
    }

    @Test
    void validarCorreo_DebeRetornarFalso_ParaCorreoInvalido() {
        assertFalse(ValidationUtil.isValidEmail("no-es-un-correo"));
    }

    @Test
    void validarCorreo_DebeRetornarFalso_ParaNulo() {
        assertFalse(ValidationUtil.isValidEmail(null));
    }
}
