package edu.eci.dosw.core.validator;

import edu.eci.dosw.core.model.User;
import edu.eci.dosw.core.util.ValidationUtil;

/**
 * Validador de la entidad {@link User}.
 * Centraliza las reglas de validación aplicables a los usuarios antes de ser registrados en el sistema.
 */
public class UserValidator {

    private UserValidator() {
        // Clase utilitaria — no se permite instanciar
    }

    /**
     * Valida que un usuario cumpla con los requisitos mínimos del sistema.
     * Verifica que el objeto no sea nulo, que el nombre esté presente y
     * que el correo electrónico, si fue proporcionado, tenga un formato válido.
     *
     * @param user usuario a validar
     * @throws IllegalArgumentException si el usuario es nulo, el nombre está vacío
     *                                  o el correo tiene un formato inválido
     */
    public static void validate(User user) {
        ValidationUtil.validateNotNull(user, "usuario");
        ValidationUtil.validateNotBlank(user.getName(), "nombre");
        if (user.getEmail() != null && !user.getEmail().isBlank()
                && !ValidationUtil.isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                    "El correo electrónico '" + user.getEmail() + "' no tiene un formato válido.");
        }
    }
}
