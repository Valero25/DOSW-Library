package edu.eci.dosw.persistence.mapper;

import edu.eci.dosw.model.Availability;
import edu.eci.dosw.model.Book;
import edu.eci.dosw.model.Metadata;
import edu.eci.dosw.persistence.entity.BookEntity;

public class BookEntityMapper {

    private BookEntityMapper() {
    }

    public static Book toDomain(BookEntity entity) {
        Book book = new Book();
        book.setId(entity.getId());
        book.setTitle(entity.getTitle());
        book.setAuthor(entity.getAuthor());
        book.setIsbn(entity.getIsbn());
        book.setAvailable(entity.isAvailable());
        book.setCategories(entity.getCategories());
        book.setPublicationType(entity.getPublicationType());
        book.setPublicationDate(entity.getPublicationDate());
        book.setDateAddedToCatalog(entity.getDateAddedToCatalog());

        if (entity.getPages() != null || entity.getLanguage() != null || entity.getPublisher() != null) {
            book.setMetadata(new Metadata(entity.getPages(), entity.getLanguage(), entity.getPublisher()));
        }

        if (entity.getTotalCopies() != null) {
            book.setAvailability(new Availability(
                    entity.getStatus(), entity.getTotalCopies(),
                    entity.getAvailableCopies(), entity.getLoanedCopies()));
        }

        return book;
    }

    public static BookEntity toEntity(Book book) {
        BookEntity entity = new BookEntity();
        entity.setId(book.getId());
        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setIsbn(book.getIsbn());
        entity.setAvailable(book.isAvailable());
        entity.setCategories(book.getCategories());
        entity.setPublicationType(book.getPublicationType());
        entity.setPublicationDate(book.getPublicationDate());
        entity.setDateAddedToCatalog(book.getDateAddedToCatalog());

        if (book.getMetadata() != null) {
            entity.setPages(book.getMetadata().getPages());
            entity.setLanguage(book.getMetadata().getLanguage());
            entity.setPublisher(book.getMetadata().getPublisher());
        }

        if (book.getAvailability() != null) {
            entity.setStatus(book.getAvailability().getStatus());
            entity.setTotalCopies(book.getAvailability().getTotalCopies());
            entity.setAvailableCopies(book.getAvailability().getAvailableCopies());
            entity.setLoanedCopies(book.getAvailability().getLoanedCopies());
        }

        return entity;
    }
}
