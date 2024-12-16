package com.RestApiWithOutDb.RestApiWithOutDb.controller;

import com.RestApiWithOutDb.RestApiWithOutDb.model.Course;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Users;
import com.RestApiWithOutDb.RestApiWithOutDb.service.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class RestControllers {

    private final Services services;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public RestControllers(Services services, AuthenticationManager authenticationManager) {
        this.services = services;
        this.authenticationManager = authenticationManager;
    }

    // Test endpoint
    @GetMapping("/hello")
    public String hello() {
        return "<h1>Success</h1>";
    }

    // User registration
    @PostMapping("/register")
    public String registerUser(@RequestBody Users user) {
        return services.createServices(user);
    }

    // User login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user) {
        try {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);

            if (authentication.isAuthenticated()) {
                return ResponseEntity.ok("Login successful!");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // Get a single user by ID
    @GetMapping("/getStudent")
    public Users getStudent(@RequestParam int id) {
        return services.getUserById(id);
    }

    // Delete a user
    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam int id) {
        services.deleteServices(id);
        return "Deleted successfully!";
    }

    // Update user details
    @PostMapping("/update")
    public Users updateUser(@RequestBody Users user) {
        return services.updateServices(user.getId(), user);
    }

    // Add a new course
    @PostMapping("/createCourse")
    public String createCourse(@RequestBody Course course) {
        return services.createCourse(course);
    }

    // Update course details
    @PostMapping("/updateCourse")
    public Course updateCourse(@RequestBody Course course) {
        return services.updateCourses(course.getId(), course);
    }

    // Get all users
    @GetMapping("/getAll")
    public List<Users> getAllUsers() {
        return services.getAllUsers();
    }

    // Get all courses
    @GetMapping("/getAllCourses")
    public List<Course> getAllCourses() {
        return services.getAllCourses();
    }

    // Assign a user to a course
    @PostMapping("/addUserToCourse")
    public String addUserToCourse(@RequestParam Integer userId, @RequestParam Integer courseId) {
        return services.addUserToCourse(userId, courseId);
    }
}
