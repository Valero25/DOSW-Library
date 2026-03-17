package edu.eci.dosw.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario en el sistema.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Construye una nueva excepción con el identificador del usuario no encontrado.
     *
     * @param userId identificador del usuario que no fue encontrado
     */
    public UserNotFoundException(String userId) {
        super("Usuario no encontrado: " + userId);
    }

    /**
     * Construye una nueva excepción con un mensaje personalizado y la causa raíz.
     *
     * @param message mensaje descriptivo del error
     * @param cause   causa raíz de la excepción
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
