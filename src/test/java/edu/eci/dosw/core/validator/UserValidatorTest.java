package edu.eci.dosw.core.validator;

import edu.eci.dosw.core.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link UserValidator}.
 */
class UserValidatorTest {

    @Test
    void validar_DebePasar_CuandoUsuarioEsValido() {
        User usuario = new User("1", "Juan", "juan@eci.edu.co");
        assertDoesNotThrow(() -> UserValidator.validate(usuario));
    }

    @Test
    void validar_DebeLanzarExcepcion_CuandoUsuarioEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validate(null));
    }

    @Test
    void validar_DebeLanzarExcepcion_CuandoNombreEstaVacio() {
        User usuario = new User("1", "  ", "juan@eci.edu.co");
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validate(usuario));
    }

    @Test
    void validar_DebeLanzarExcepcion_CuandoCorreoTieneFormatoInvalido() {
        User usuario = new User("1", "Juan", "correo-invalido");
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validate(usuario));
    }

    @Test
    void validar_DebePasar_CuandoCorreoEsNulo() {
        User usuario = new User("1", "Juan", null);
        assertDoesNotThrow(() -> UserValidator.validate(usuario));
    }
}
