package com.cognizant.student.controller;

import com.cognizant.student.model.Student;
import com.cognizant.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller to expose endpoints for managing students.
 * @RequestMapping defines a base URL for all endpoints in this controller.
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    // Inject the repository to interact with the database
    @Autowired
    private StudentRepository studentRepository;

    /**
     * GET /api/students: Get a list of all students.
     * @return A list of all students.
     */
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * POST /api/students: Add a new student.
     * @param student The student data from the request body.
     * @return The newly created student.
     */
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    /**
     * GET /api/students/{id}: Get a student by their ID.
     * @param id The ID of the student.
     * @return The student if found, otherwise a 404 Not Found response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/students/{id}: Update an existing student's details.
     * @param id The ID of the student to update.
     * @param studentDetails The new details for the student.
     * @return The updated student if found, otherwise a 404 Not Found response.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(studentDetails.getName());
                    student.setEmail(studentDetails.getEmail());
                    student.setAge(studentDetails.getAge());
                    Student updatedStudent = studentRepository.save(student);
                    return ResponseEntity.ok(updatedStudent);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/students/{id}: Delete a student.
     * @param id The ID of the student to delete.
     * @return A 204 No Content response if successful, otherwise 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    studentRepository.delete(student);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
