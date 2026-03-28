package edu.eci.dosw.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la disponibilidad de un libro.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de disponibilidad de un libro")
public class AvailabilityDTO {

    @Schema(description = "Estado de disponibilidad del libro", example = "Disponible")
    private String status;

    @Schema(description = "Número total de copias del libro", example = "5")
    private Integer totalCopies;

    @Schema(description = "Número de copias disponibles para préstamo", example = "3")
    private Integer availableCopies;

    @Schema(description = "Número de copias actualmente prestadas", example = "2")
    private Integer loanedCopies;
}
