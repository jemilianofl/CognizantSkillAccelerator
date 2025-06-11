package com.cognizant.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping("/hello-user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String sayHelloUser() {
        return "Hello from a secured USER endpoint!";
    }

    @GetMapping("/hello-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String sayHelloAdmin() {
        return "Hello from a secured ADMIN endpoint!";
    }
}