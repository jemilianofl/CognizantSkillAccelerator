package com.cognizant.library.model;

import jakarta.persistence.*;
import java.time.*;

/**
 * Represents a Book entity.
 * The @Entity annotation specifies that this class is an entity and is mapped to a database table.
 */
@Entity
public class Book {

    /**
     * The unique identifier for the book.
     * @Id marks this field as the primary key.
     * @GeneratedValue specifies that the ID should be generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The title of the book.
     * @Column specifies the mapping of the field to a database column.
     * nullable = false means this column cannot be null.
     */
    @Column(nullable = false)
    private String title;

    /**
     * The author of the book.
     */
    @Column(nullable = false)
    private String author;

    /**
     * The ISBN of the book. The unique = true constraint ensures no two books have the same ISBN.
     */
    @Column(unique = true)
    private String isbn;

    /**
     * The publication date of the book.
     */
    private LocalDate publishedDate;

    // --- Getters and Setters ---
    // Standard methods to get and set the values of the fields.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }
}