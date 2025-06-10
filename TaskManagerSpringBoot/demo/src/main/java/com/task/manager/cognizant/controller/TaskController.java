package com.task.manager.cognizant.controller;

import com.task.manager.cognizant.model.Task;
import com.task.manager.cognizant.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // Marks this class as a REST controller, which handles HTTP requests
@RequestMapping("/api/tasks") // All endpoints in this controller will start with /api/tasks
public class TaskController {

    @Autowired // Automatically injects an instance of TaskRepository
    private TaskRepository taskRepository;

    // --- Endpoint to Get All Tasks ---
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // --- Endpoint to Get a Single Task by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskRepository.findById(id);
        // If task is found, return 200 OK with the task body.
        // If not found, return 404 Not Found.
        return task.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- Endpoint to Add a New Task ---
    @PostMapping
    public ResponseEntity<Task> addTask(@Valid @RequestBody Task task) {
        // @Valid triggers validation on the Task object.
        // @RequestBody maps the request body to the Task object.
        Task savedTask = taskRepository.save(task);
        // Return 201 Created with the newly created task in the body.
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    // --- Endpoint to Update an Existing Task ---
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTitle(taskDetails.getTitle());
            existingTask.setDescription(taskDetails.getDescription());
            existingTask.setCompleted(taskDetails.isCompleted());
            Task updatedTask = taskRepository.save(existingTask);
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Endpoint to Delete a Task ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            // Return 204 No Content for successful deletion.
            return ResponseEntity.noContent().build();
        } else {
            // Return 404 Not Found if the task doesn't exist.
            return ResponseEntity.notFound().build();
        }
    }
}