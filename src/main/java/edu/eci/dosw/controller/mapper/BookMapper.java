package edu.eci.dosw.controller.mapper;

import edu.eci.dosw.controller.dto.BookDTO;
import edu.eci.dosw.controller.dto.MetadataDTO;
import edu.eci.dosw.controller.dto.AvailabilityDTO;
import edu.eci.dosw.core.model.Book;
import edu.eci.dosw.core.model.Metadata;
import edu.eci.dosw.core.model.Availability;

/**
 * Clase responsable de convertir entre la entidad {@link Book} y su correspondiente
 * objeto de transferencia de datos {@link BookDTO}.
 * Desacopla la representación interna del dominio de la representación expuesta por la API REST.
 */
public class BookMapper {

    /**
     * Constructor privado para evitar la instanciación de esta clase utilitaria.
     */
    private BookMapper() {
    }

    /**
     * Convierte una entidad {@link Book} en su representación {@link BookDTO}.
     *
     * @param book entidad libro a convertir; no debe ser nula
     * @return DTO con los datos del libro
     */
    public static BookDTO toDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setAvailable(book.isAvailable());
        
        if (book.getCategories() != null) {
            dto.setCategories(book.getCategories());
        }
        
        if (book.getPublicationType() != null) {
            dto.setPublicationType(book.getPublicationType().toString());
        }
        
        dto.setPublicationDate(book.getPublicationDate());
        
        if (book.getMetadata() != null) {
            MetadataDTO metadataDTO = new MetadataDTO();
            metadataDTO.setPages(book.getMetadata().getPages());
            metadataDTO.setLanguage(book.getMetadata().getLanguage());
            metadataDTO.setPublisher(book.getMetadata().getPublisher());
            dto.setMetadata(metadataDTO);
        }
        
        if (book.getAvailability() != null) {
            AvailabilityDTO availabilityDTO = new AvailabilityDTO();
            availabilityDTO.setStatus(book.getAvailability().getStatus());
            availabilityDTO.setTotalCopies(book.getAvailability().getTotalCopies());
            availabilityDTO.setAvailableCopies(book.getAvailability().getAvailableCopies());
            availabilityDTO.setLoanedCopies(book.getAvailability().getLoanedCopies());
            dto.setAvailability(availabilityDTO);
        }
        
        dto.setDateAddedToCatalog(book.getDateAddedToCatalog());
        
        return dto;
    }

    /**
     * Convierte un {@link BookDTO} en la entidad de dominio {@link Book}.
     *
     * @param dto DTO del libro a convertir; no debe ser nulo
     * @return entidad libro correspondiente al DTO
     */
    public static Book toModel(BookDTO dto) {
        Book book = new Book(
                dto.getId(),
                dto.getTitle(),
                dto.getAuthor(),
                dto.getIsbn()
        );
        
        book.setAvailable(dto.isAvailable());
        book.setCategories(dto.getCategories());
        
        if (dto.getPublicationType() != null) {
            try {
                book.setPublicationType(Enum.valueOf(edu.eci.dosw.core.model.enums.PublicationType.class, dto.getPublicationType()));
            } catch (IllegalArgumentException e) {
                book.setPublicationType(null);
            }
        }
        
        book.setPublicationDate(dto.getPublicationDate());
        
        if (dto.getMetadata() != null) {
            Metadata metadata = new Metadata();
            metadata.setPages(dto.getMetadata().getPages());
            metadata.setLanguage(dto.getMetadata().getLanguage());
            metadata.setPublisher(dto.getMetadata().getPublisher());
            book.setMetadata(metadata);
        }
        
        if (dto.getAvailability() != null) {
            Availability availability = new Availability();
            availability.setStatus(dto.getAvailability().getStatus());
            availability.setTotalCopies(dto.getAvailability().getTotalCopies());
            availability.setAvailableCopies(dto.getAvailability().getAvailableCopies());
            availability.setLoanedCopies(dto.getAvailability().getLoanedCopies());
            book.setAvailability(availability);
        }
        
        book.setDateAddedToCatalog(dto.getDateAddedToCatalog());
        
        return book;
    }
}
