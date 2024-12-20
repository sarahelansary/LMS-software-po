package com.RestApiWithOutDb.RestApiWithOutDb.controller;

import com.RestApiWithOutDb.RestApiWithOutDb.model.Course;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Lesson;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Users;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Notification;
import com.RestApiWithOutDb.RestApiWithOutDb.service.EmailService;
import com.RestApiWithOutDb.RestApiWithOutDb.service.NotificationService;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Question;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Quiz;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Assignment;
import com.RestApiWithOutDb.RestApiWithOutDb.service.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final NotificationService notificationService;

    private  final EmailService emailService;


    @Autowired

    public RestControllers(Services services, NotificationService notificationService,
                           AuthenticationManager authenticationManager, EmailService emailService) {
        this.services = services;
        this.authenticationManager = authenticationManager;
        this.notificationService = notificationService;
        this.emailService = emailService; // Fix typo here
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
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR','STUDENT')")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(),
                    user.getPassword());
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
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @GetMapping("/getStudent")
    public Users getStudent(@RequestParam int id) {
        return services.getUserById(id);
    }

    // Delete a user
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam int id) {
        services.deleteServices(id);
        return "Deleted successfully!";
    }

    // Update user details
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR','STUDENT')")
    @PostMapping("/update")
    public Users updateUser(@RequestBody Users user) {
        return services.updateServices(user.getId(), user);
    }

    // ======================================================

    // Add a new course
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping("/createCourse")
    public String createCourse(@RequestBody Course course) {
        return services.createCourse(course);
    }

    // Update course details
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping("/addUserToCourse")
    public String addUserToCourse(@RequestParam Integer userId, @RequestParam Integer courseId) {
        return services.addUserToCourse(userId, courseId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @DeleteMapping("/deleteCourse")
    public String deleteCourse(@RequestParam int id) {
        services.deleteCourse(id);
        // Notify students about the course deletion
        List<Users> students = services.getAllUsers();
        for (Users student : students) {
            if (student.getRole().contains("STUDENT")) {
                notificationService.addNotification("Course " + id + " has been deleted", student.getUsername());
                emailService.sendEmail(student.getEmail(), "Course deletion", "Course " + id + " has been deleted");
            }
            notificationService.addNotification("Course " + id + " has been deleted", student.getUsername());
            emailService.sendEmail(student.getEmail(), "Course deletion", "Course " + id + " has been deleted");
        }
        return "delete Successfully";
    }
    

    // Add media file to a course
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping("/{courseId}/addMedia")
    public String addMediaFile(@PathVariable int courseId, @RequestParam String mediaFile) {
        boolean res = services.addMediaFile(courseId, mediaFile);
        if (!res) {
            return "The course is not exist!";
        }
        // Notify students about the new media
        List<Users> students = services.getAllUsers();
        for (Users student : students) {
            if (student.getRole().contains("STUDENT")||student.getRole().contains("student")) {
                notificationService.addNotification("New media file added to course " + courseId, student.getUsername());
                emailService.sendEmail(student.getEmail(), "media added", "New media file added to course " + courseId);

            }
             notificationService.addNotification("New media file added to course " + courseId, student.getUsername());


        }
        return "Media added Successfully";
    }

    // Add lesson to a course
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping("/{courseId}/addLesson")
    public String addLesson(@PathVariable int courseId, @RequestBody Lesson lesson) {
        boolean res = services.addLesson(courseId, lesson);
        if (!res) {
            return "The course is not exist!";
        }
         // Notify students about the new lesson
         List<Users> students = services.getAllUsers();
         for (Users student : students) {
             if (student.getRole().contains("student")||student.getRole().contains("STUDENT")) {
                 notificationService.addNotification("New lesson added to course " + courseId, student.getUsername());
                 emailService.sendEmail(student.getEmail(), "new lesson added", "New lesson added to course " + courseId);
             }
              notificationService.addNotification("New lesson added to course " + courseId, student.getUsername());

         }
        return "Lesson added Successfully";
    }

    // Attend student to a lesson
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/attendStudent")
    public String attendStudent(@RequestParam int userId, @RequestParam int courseId,
            @RequestParam int lessonId, @RequestParam String OTP) {
        boolean res = services.attendLesson(userId, courseId, lessonId, OTP);
        if (!res) {
            return "The lesson is not exist or wrong Password!";
        }
        // Notify instructor about the student's attendance
        List<Users> instructors = services.getAllUsers();
        for (Users instructor : instructors) {
            if (instructor.getRole().contains("INSTRUCTOR") || instructor.getRole().contains("instructor")) {
                notificationService.addNotification("Student with ID " + userId + " attended lesson " + lessonId + " in course " + courseId, instructor.getUsername());
                emailService.sendEmail(instructor.getEmail(),
                        "new student attended",
                        "Student with ID " + userId + " attended lesson " + lessonId + " in course ");
            }
            notificationService.addNotification("Student with ID " + userId + " attended lesson " + lessonId + " in course " + courseId, instructor.getUsername());
        }

        return "Student attended Successfully";
    }

    // Get all lessons from a course
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR','STUDENT')")
    @GetMapping("/getAllLessons")
    public List<Lesson> getAllLesson(@RequestParam int courseId) {
        return services.getAllLessons(courseId);
    }

    // Existing methods remain unchanged...

    // Add Notification endpoint
    @PostMapping("/notify")
    public String sendNotification(@RequestParam String message, @RequestParam String recipientUsername) {
        notificationService.addNotification(message, recipientUsername);

        int messageId = notificationService.addNotification(message, recipientUsername);
        return "Notification sent successfully! Message ID: " + messageId;
    }

    // Get notifications for a user
    @GetMapping("/notifications")
    public List<Notification> getUserNotifications(@RequestParam String username,
            @RequestParam(required = false, defaultValue = "false") boolean unreadOnly) {
        return notificationService.getNotificationsForUser(username, unreadOnly);
    }

    // Mark a notification as read
    @PostMapping("/notifications/markRead")
    public ResponseEntity<String> markNotificationAsRead(@RequestParam Integer id) {
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok("Notification marked as read.");
    }

    // Create a new quiz
    @PostMapping("/createQuiz")
    public String createQuiz(@RequestBody Quiz quiz) {
        return services.createQuiz(quiz);
    }

    // Add question to a quiz
    @PostMapping("/addQuestionToQuiz")
    public String addQuestionToQuiz(@RequestParam Integer quizId, @RequestBody Question question) {
        return services.addQuestionToQuiz(quizId, question);
    }

    // Submit assignment
    @PostMapping("/submitAssignment")
    public String submitAssignment(@RequestParam Integer studentId, @RequestParam Integer courseId,
            @RequestParam String fileUrl) {
        return services.submitAssignment(studentId, courseId, fileUrl);
    }

    // Grade assignment
    @PostMapping("/gradeAssignment")
    public String gradeAssignment(@RequestParam Integer assignmentId, @RequestParam Integer grade,
            @RequestParam String feedback) {
        return services.gradeAssignment(assignmentId, grade, feedback);
    }

    // Get all quizzes
    @GetMapping("/getAllQuizzes")
    public List<Quiz> getAllQuizzes() {
        return services.getAllQuizzes();
    }

    // Get all assignments
    // @GetMapping("/getAllAssignments")
    // public List<Assignment> getAllAssignments() {
    // return services.getAllAssignments();
    // }

}
