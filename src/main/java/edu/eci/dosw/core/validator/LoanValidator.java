package edu.eci.dosw.core.validator;

import edu.eci.dosw.core.util.ValidationUtil;

/**
 * Validador de los datos de entrada para las operaciones de préstamo.
 * Verifica que los identificadores requeridos para crear o consultar un préstamo sean válidos.
 */
public class LoanValidator {

    private LoanValidator() {
        // Clase utilitaria — no se permite instanciar
    }

    /**
     * Valida que los identificadores de libro y usuario no sean nulos ni estén vacíos,
     * condición necesaria para poder crear un préstamo.
     *
     * @param bookId identificador del libro a prestar
     * @param userId identificador del usuario solicitante
     * @throws IllegalArgumentException si alguno de los identificadores es nulo o está vacío
     */
    public static void validateIds(String bookId, String userId) {
        ValidationUtil.validateNotBlank(bookId, "identificador del libro");
        ValidationUtil.validateNotBlank(userId, "identificador del usuario");
    }
}
