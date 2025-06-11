package com.cognizant.logging;

import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/users")
public class UserController {

    // Get a logger instance for this class
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // Use a thread-safe map as a simple in-memory database
    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public UserController() {
        // Pre-populate with some data
        long id1 = counter.incrementAndGet();
        users.put(id1, new User(id1, "Alice"));
        long id2 = counter.incrementAndGet();
        users.put(id2, new User(id2, "Bob"));
        logger.info("Initialized with 2 default users.");
    }

    /**
     * GET /users : Retrieve all users.
     */
    @GetMapping
    public List<User> getAllUsers() {
        logger.debug("Entering getAllUsers method."); // This will only show in 'dev'
        logger.info("Retrieving all {} users.", users.size());
        return new ArrayList<>(users.values());
    }

    /**
     * POST /users : Add a new user.
     */
    @PostMapping
    public User addUser(@RequestBody User user) {
        logger.debug("Entering addUser method with user name: {}", user.getName());
        long newId = counter.incrementAndGet();
        user.setId(newId);
        users.put(newId, user);
        logger.info("Successfully added user with ID: {}", newId);
        return user;
    }

    /**
     * DELETE /users/{id} : Delete a user.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.debug("Entering deleteUser method for ID: {}", id);
        if (users.containsKey(id)) {
            users.remove(id);
            logger.info("Successfully deleted user with ID: {}", id);
            return ResponseEntity.ok().build();
        } else {
            // This is a client error, but we log it as a warning for visibility.
            logger.warn("Attempted to delete non-existent user with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}