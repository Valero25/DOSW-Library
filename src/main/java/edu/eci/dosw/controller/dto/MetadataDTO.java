package edu.eci.dosw.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la metadata de un libro.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de metadata de un libro")
public class MetadataDTO {

    @Schema(description = "Número de páginas del libro", example = "250")
    private Integer pages;

    @Schema(description = "Idioma en que está escrito el libro", example = "Español")
    private String language;

    @Schema(description = "Editorial que publicó el libro", example = "Penguin Books")
    private String publisher;
}
