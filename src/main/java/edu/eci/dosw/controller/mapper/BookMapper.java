package edu.eci.dosw.controller.mapper;

import edu.eci.dosw.controller.dto.BookDTO;
import edu.eci.dosw.core.model.Book;

/**
 * Clase responsable de convertir entre la entidad {@link Book} y su correspondiente
 * objeto de transferencia de datos {@link BookDTO}.
 * Desacopla la representación interna del dominio de la representación expuesta por la API REST.
 */
public class BookMapper {

    private BookMapper() {
        // Clase utilitaria — no se permite instanciar
    }

    /**
     * Convierte una entidad {@link Book} en su representación {@link BookDTO}.
     *
     * @param book entidad libro a convertir; no debe ser nula
     * @return DTO con los datos del libro
     */
    public static BookDTO toDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.isAvailable()
        );
    }

    /**
     * Convierte un {@link BookDTO} en la entidad de dominio {@link Book}.
     *
     * @param dto DTO del libro a convertir; no debe ser nulo
     * @return entidad libro correspondiente al DTO
     */
    public static Book toModel(BookDTO dto) {
        return new Book(
                dto.getId(),
                dto.getTitle(),
                dto.getAuthor(),
                dto.getIsbn()
        );
    }
}
