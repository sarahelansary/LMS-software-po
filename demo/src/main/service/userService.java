package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.User;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    public List<Course> getCoursesByStudent(Long studentId) {
        return courseRepository.findByStudentsId(studentId);
    }

    public Optional<Course> enrollStudent(Long courseId, User student) {
        Optional<Course> course = courseRepository.findById(courseId);
        course.ifPresent(c -> {
            c.getStudents().add(student);
            courseRepository.save(c);
        });
        return course;
    }
}
