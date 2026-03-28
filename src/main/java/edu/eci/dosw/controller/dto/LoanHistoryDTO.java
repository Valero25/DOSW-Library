package edu.eci.dosw.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO que representa un evento en el historial de un préstamo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Evento del historial de un préstamo")
public class LoanHistoryDTO {

    @Schema(description = "Estado del evento del préstamo", example = "Prestado")
    private String status;

    @Schema(description = "Fecha en que ocurrió el evento", example = "2026-03-27")
    private LocalDate date;
}
