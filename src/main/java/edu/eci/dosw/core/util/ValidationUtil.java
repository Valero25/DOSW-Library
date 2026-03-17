package edu.eci.dosw.core.util;

/**
 * Clase utilitaria que provee métodos de validación comunes para el sistema de la biblioteca.
 * No puede ser instanciada; todos sus métodos son estáticos.
 */
public class ValidationUtil {

    private ValidationUtil() {
        // Clase utilitaria — no se permite instanciar
    }

    /**
     * Valida que una cadena de texto no sea nula ni esté vacía.
     *
     * @param value     valor a validar
     * @param fieldName nombre del campo, utilizado en el mensaje de error
     * @throws IllegalArgumentException si el valor es nulo o está vacío
     */
    public static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no debe ser nulo ni estar vacío.");
        }
    }

    /**
     * Valida que un objeto no sea nulo.
     *
     * @param value     objeto a validar
     * @param fieldName nombre del campo, utilizado en el mensaje de error
     * @throws IllegalArgumentException si el valor es nulo
     */
    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " no debe ser nulo.");
        }
    }

    /**
     * Verifica que una cadena tenga formato ISBN-13 válido.
     *
     * @param isbn código ISBN a validar
     * @return {@code true} si el ISBN tiene formato válido; {@code false} en caso contrario
     */
    public static boolean isValidIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return false;
        }
        String cleaned = isbn.replaceAll("[- ]", "");
        return cleaned.matches("\\d{13}");
    }

    /**
     * Verifica que una dirección de correo electrónico tenga un formato básico válido.
     *
     * @param email dirección de correo a validar
     * @return {@code true} si el formato del correo es válido; {@code false} en caso contrario
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }
}
