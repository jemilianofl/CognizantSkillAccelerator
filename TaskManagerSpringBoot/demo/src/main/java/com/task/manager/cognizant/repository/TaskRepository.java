package com.task.manager.cognizant.repository;

import com.task.manager.cognizant.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Marks this as a Spring repository bean
public interface TaskRepository extends JpaRepository<Task, Long> {
    // JpaRepository<Task, Long> gives us all the standard CRUD methods.
    // - Task: The entity type the repository manages.
    // - Long: The type of the primary key of the Task entity.
}