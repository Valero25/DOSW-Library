package edu.eci.dosw.core.util;

import java.time.LocalDate;

/**
 * Clase utilitaria que provee métodos relacionados con el manejo de fechas en el sistema.
 * No puede ser instanciada; todos sus métodos son estáticos.
 */
public class DateUtil {

    /**
     * Constructor privado para evitar la instanciación de esta clase utilitaria.
     */
    private DateUtil() {
    }

    /**
     * Retorna la fecha actual del sistema.
     *
     * @return fecha de hoy según el reloj del sistema
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * Determina si un préstamo está vencido, comparando la fecha de inicio con la fecha actual.
     *
     * @param loanDate fecha en que se realizó el préstamo
     * @param maxDays  número máximo de días permitidos para el préstamo
     * @return {@code true} si han transcurrido más de {@code maxDays} desde {@code loanDate};
     *         {@code false} en caso contrario
     */
    public static boolean isOverdue(LocalDate loanDate, int maxDays) {
        if (loanDate == null) {
            return false;
        }
        return LocalDate.now().isAfter(loanDate.plusDays(maxDays));
    }
}
