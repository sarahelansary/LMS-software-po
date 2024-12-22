package com.RestApiWithOutDb.RestApiWithOutDb;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.RestApiWithOutDb.RestApiWithOutDb.model.Users;
import com.RestApiWithOutDb.RestApiWithOutDb.service.Services;

class UserMangmentTest {

    private Services services;

    @BeforeEach
    void setUp() {
        services = new Services(null, null); 
    }

    @Test
    void testCreateUser() {
        Users newUser = new Users(null, "jane_doe", "securePass", "student", "jane@example.com", new HashSet<>());

        String response = services.createServices(newUser);

        assertEquals("User successfully registered with ID: 2", response);
        assertEquals(2, services.getAllUsers().size());
        assertEquals("jane_doe", services.getUserById(2).getUsername());
    }

    @Test
    void testGetUserById() {
        Users user = services.getUserById(1);

        assertNotNull(user);
        assertEquals("john_doe", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void testUpdateUser() {
        Users updatedUser = new Users(1, "john_updated", "newPassword123", "student", "john_updated@example.com", new HashSet<>());

        Users result = services.updateServices(1, updatedUser);

        assertEquals("john_updated", result.getUsername());
        assertEquals("john_updated@example.com", result.getEmail());
    }

    @Test
    void testDeleteUser() {
        String response = services.deleteServices(1);

        assertEquals("Successfully deleted user", response);
        assertEquals(0, services.getAllUsers().size());
    }

    @Test
    void testDuplicateEmailError() {
        Users duplicateUser = new Users(null, "john_doe_2", "password123", "student", "john@example.com", new HashSet<>());

        String response = services.createServices(duplicateUser);

        assertEquals("Email already exists!", response);
    }

    @Test
    void testGetAllUsers() {
        List<Users> users = services.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("john_doe", users.get(0).getUsername());
    }
}
