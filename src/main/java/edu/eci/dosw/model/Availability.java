package edu.eci.dosw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la información de disponibilidad de un libro.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Availability {

    private String status; // "Disponible", "Agotado", "En proceso", etc.
    private Integer totalCopies;
    private Integer availableCopies;
    private Integer loanedCopies;
}
