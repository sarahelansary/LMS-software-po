package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = userService.createCourse(course);
        return ResponseEntity.ok(createdCourse);
    }

    @GetMapping("/instructors/{instructorId}/courses")
    public ResponseEntity<List<Course>> getCoursesByInstructor(@PathVariable Long instructorId) {
        List<Course> courses = userService.getCoursesByInstructor(instructorId);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/courses/{courseId}/enroll")
    public ResponseEntity<String> enrollStudent(@PathVariable Long courseId, @RequestBody User student) {
        Optional<Course> course = userService.enrollStudent(courseId, student);
        if (course.isPresent()) {
            return ResponseEntity.ok("Student enrolled successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
