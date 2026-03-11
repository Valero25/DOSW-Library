package edu.eci.dosw.model;

/**
 * Represents a book in the library system.
 */
public class Book {

    private String id;
    private String title;
    private String author;
    private String isbn;
    private boolean available;

    public Book() {
    }

    public Book(String id, String title, String author, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Book{id='" + id + "', title='" + title + "', author='" + author
                + "', isbn='" + isbn + "', available=" + available + "}";
    }
}
