package com.cognizant.student.repository;

import com.cognizant.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for handling Student data persistence.
 * JpaRepository provides all the necessary CRUD methods (save, findById, findAll, delete, etc.)
 * out of the box. We don't need to write any implementation code.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Spring Data JPA will automatically implement this interface
    // with methods to work with the Student entity (whose primary key is of type Long).
}