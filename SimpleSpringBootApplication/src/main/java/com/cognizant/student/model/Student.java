package com.cognizant.student.model;

import jakarta.persistence.*;

/**
 * Represents a Student entity.
 * The @Entity annotation tells Spring Data JPA that this class should be mapped to a database table.
 */
@Entity
public class Student {

    /**
     * The unique identifier for the student.
     * @Id marks this field as the primary key.
     * @GeneratedValue tells JPA how the ID is generated. IDENTITY is a common strategy
     * that relies on the database's auto-increment column.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private int age;

    // --- Getters and Setters ---
    // These are required by JPA to create instances of the entity.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}