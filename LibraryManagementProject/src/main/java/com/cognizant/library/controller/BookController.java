package com.cognizant.library.controller;

import com.cognizant.library.model.Book;
import com.cognizant.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST Controller for managing books.
 * @RestController combines @Controller and @ResponseBody, which eliminates the need
 * to annotate every request handling method with @ResponseBody.
 * @RequestMapping("/api/books") maps all requests starting with /api/books to this controller.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    // @Autowired injects the BookRepository bean so we can use it.
    @Autowired
    private BookRepository bookRepository;

    /**
     * POST /api/books: Add a new book.
     * @param book The book object to be created.
     * @return The created book with a 201 (Created) status.
     */
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    /**
     * GET /api/books: Retrieve all books.
     * @return A list of all books.
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * GET /api/books/{id}: Retrieve a single book by its ID.
     * @param id The ID of the book to retrieve.
     * @return The book if found (200 OK), or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/books/{id}: Update an existing book.
     * @param id The ID of the book to update.
     * @param bookDetails The updated book information.
     * @return The updated book if found (200 OK), or 404 Not Found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setIsbn(bookDetails.getIsbn());
                    book.setPublishedDate(bookDetails.getPublishedDate());
                    Book updatedBook = bookRepository.save(book);
                    return ResponseEntity.ok(updatedBook);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/books/{id}: Delete a book by its ID.
     * @param id The ID of the book to delete.
     * @return 204 No Content if successful, or 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}