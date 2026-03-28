package edu.eci.dosw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Representa el historial de eventos en un préstamo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanHistory {

    private String status; // "Prestado", "Devuelto", "Atrasado", etc.
    private LocalDate date;
}
