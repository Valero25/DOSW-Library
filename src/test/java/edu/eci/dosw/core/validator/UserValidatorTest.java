package edu.eci.dosw.core.validator;

import edu.eci.dosw.core.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link UserValidator}.
 */
class UserValidatorTest {

    private User createUser(String id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    @Test
    void validar_DebePasar_CuandoUsuarioEsValido() {
        User usuario = createUser("1", "Juan", "juan@eci.edu.co");
        assertDoesNotThrow(() -> UserValidator.validate(usuario));
    }

    @Test
    void validar_DebeLanzarExcepcion_CuandoUsuarioEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validate(null));
    }

    @Test
    void validar_DebeLanzarExcepcion_CuandoNombreEstaVacio() {
        User usuario = createUser("1", "  ", "juan@eci.edu.co");
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validate(usuario));
    }

    @Test
    void validar_DebeLanzarExcepcion_CuandoCorreoTieneFormatoInvalido() {
        User usuario = createUser("1", "Juan", "correo-invalido");
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validate(usuario));
    }

    @Test
    void validar_DebePasar_CuandoCorreoEsNulo() {
        User usuario = createUser("1", "Juan", null);
        assertDoesNotThrow(() -> UserValidator.validate(usuario));
    }
}
