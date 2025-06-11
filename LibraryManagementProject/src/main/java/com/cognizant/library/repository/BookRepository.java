package com.cognizant.library.repository;

import com.cognizant.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Book entities.
 * By extending JpaRepository, we get a lot of standard CRUD methods for free,
 * such as save(), findById(), findAll(), deleteById(), etc.
 *
 * @Repository annotation marks this interface as a Spring component.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // JpaRepository<Book, Long> means this repository works with the Book entity,
    // and the type of the primary key is Long.
}
