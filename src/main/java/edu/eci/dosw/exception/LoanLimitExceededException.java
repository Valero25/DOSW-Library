package edu.eci.dosw.exception;

/**
 * Excepción lanzada cuando un usuario supera el número máximo de préstamos activos permitidos.
 */
public class LoanLimitExceededException extends RuntimeException {

    /**
     * Construye una nueva excepción indicando el identificador del usuario que superó el límite.
     *
     * @param userId identificador del usuario que excedió el límite de préstamos
     */
    public LoanLimitExceededException(String userId) {
        super("El usuario '" + userId + "' ha alcanzado el límite máximo de préstamos activos.");
    }

    /**
     * Construye una nueva excepción con un mensaje personalizado y la causa raíz.
     *
     * @param message mensaje descriptivo del error
     * @param cause   causa raíz de la excepción
     */
    public LoanLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
