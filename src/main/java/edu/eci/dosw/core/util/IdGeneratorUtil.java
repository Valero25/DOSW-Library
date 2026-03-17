package edu.eci.dosw.core.util;

import java.util.UUID;

/**
 * Clase utilitaria para la generación de identificadores únicos en el sistema de la biblioteca.
 * No puede ser instanciada; todos sus métodos son estáticos.
 */
public class IdGeneratorUtil {

    private IdGeneratorUtil() {
        // Clase utilitaria — no se permite instanciar
    }

    /**
     * Genera un identificador único universal (UUID) en formato de cadena de texto.
     *
     * @return cadena que representa un UUID generado aleatoriamente
     */
    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
