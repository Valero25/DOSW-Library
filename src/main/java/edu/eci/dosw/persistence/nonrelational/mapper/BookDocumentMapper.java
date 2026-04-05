package edu.eci.dosw.persistence.nonrelational.mapper;

import edu.eci.dosw.model.Availability;
import edu.eci.dosw.model.Book;
import edu.eci.dosw.model.Metadata;
import edu.eci.dosw.persistence.nonrelational.document.BookDocument;

public class BookDocumentMapper {

    private BookDocumentMapper() {}

    public static Book toDomain(BookDocument doc) {
        Book book = new Book();
        book.setId(doc.getId());
        book.setTitle(doc.getTitle());
        book.setAuthor(doc.getAuthor());
        book.setIsbn(doc.getIsbn());
        book.setAvailable(doc.isAvailable());
        book.setCategories(doc.getCategories());
        book.setPublicationType(doc.getPublicationType());
        book.setPublicationDate(doc.getPublicationDate());
        book.setDateAddedToCatalog(doc.getDateAddedToCatalog());
        if (doc.getMetadata() != null) {
            book.setMetadata(new Metadata(doc.getMetadata().getPages(),
                    doc.getMetadata().getLanguage(), doc.getMetadata().getPublisher()));
        }
        if (doc.getAvailability() != null) {
            book.setAvailability(new Availability(doc.getAvailability().getStatus(),
                    doc.getAvailability().getTotalCopies(), doc.getAvailability().getAvailableCopies(),
                    doc.getAvailability().getLoanedCopies()));
        }
        return book;
    }

    public static BookDocument toDocument(Book book) {
        BookDocument doc = new BookDocument();
        doc.setId(book.getId());
        doc.setTitle(book.getTitle());
        doc.setAuthor(book.getAuthor());
        doc.setIsbn(book.getIsbn());
        doc.setAvailable(book.isAvailable());
        doc.setCategories(book.getCategories());
        doc.setPublicationType(book.getPublicationType());
        doc.setPublicationDate(book.getPublicationDate());
        doc.setDateAddedToCatalog(book.getDateAddedToCatalog());
        if (book.getMetadata() != null) {
            doc.setMetadata(new BookDocument.MetadataEmbedded(book.getMetadata().getPages(),
                    book.getMetadata().getLanguage(), book.getMetadata().getPublisher()));
        }
        if (book.getAvailability() != null) {
            doc.setAvailability(new BookDocument.AvailabilityEmbedded(book.getAvailability().getStatus(),
                    book.getAvailability().getTotalCopies(), book.getAvailability().getAvailableCopies(),
                    book.getAvailability().getLoanedCopies()));
        }
        return doc;
    }
}
