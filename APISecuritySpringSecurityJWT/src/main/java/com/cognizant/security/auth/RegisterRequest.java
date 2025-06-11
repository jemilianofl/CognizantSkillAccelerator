package com.cognizant.security.auth;

import com.cognizant.security.Role;

public record RegisterRequest(String username, String password, Role role) {}