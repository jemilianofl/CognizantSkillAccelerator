package com.task.manager.cognizant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity // Marks this class as a JPA entity (a database table)
public class Task {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID
    private Long id;

    @NotBlank(message = "Title cannot be empty") // Validation: ensures title is not null or whitespace
    @Column(nullable = false) // Database level: ensures the column is not null
    private String title;

    private String description;

    private boolean completed = false; // Default status is not completed

    @Column(updatable = false) // This field should not be updated after creation
    private LocalDateTime createdDate;

    // This method is called before the entity is saved to the database for the first time
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    // Standard methods to get and set the private fields.
    // Your IDE can generate these for you!

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}