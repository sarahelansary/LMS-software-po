package com.RestApiWithOutDb.RestApiWithOutDb.service;

import com.RestApiWithOutDb.RestApiWithOutDb.model.Course;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Lesson;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Users;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public class Services {
    private static int idCounter = 1;
    private static int lessonCounter = 1;
    private List<Users> userslist = new ArrayList<>();
    private List<Course> courseslist = new ArrayList<>();
    private final NotificationService notificationService;

    public Services(NotificationService notificationService) {
        this.notificationService = notificationService;

        // Adding default courses
        courseslist.add(new Course(idCounter++, "Mathematics", "A math course", "36 hours", new HashSet<>()));

        // Adding default users
        userslist.add(new Users(idCounter++, "john_doe", "password123", "student", "john@example.com", new HashSet<>()));
    }

    public String hello() {
        return "<h1>Success</h1>";
    }

    public String createServices(Users user) {
        Optional<Users> existingUser = userslist.stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findFirst();

        if (existingUser.isPresent()) {
            return "Email already exists!";
        }

        user.setId(idCounter++);
        userslist.add(user);
        return "User successfully registered with ID: " + user.getId();
    }

    public String createCourse(Course course) {
        course.setId(idCounter++);
        courseslist.add(course);
        return "Course successfully created";
    }

    public String addUserToCourse(Integer userId, Integer courseId) {
        Users user = getUserById(userId);
        Course course = getCourseById(courseId);

        if (user != null && course != null) {
            course.getStudents().add(user);
            user.getCourses().add(course);

            // Add a notification
            notificationService.addNotification(
                    "You have been added to the course: " + course.getName(),
                    user.getUsername()
            );
            return "User successfully added to course";
        } else {
            return "User or course not found";
        }
    }

    public Course getCourseById(Integer courseId) {
        for (Course course : courseslist) {
            if (course.getId().equals(courseId)) {
                return course;
            }
        }
        throw new IllegalArgumentException("Course not found");
    }

    public Users getUserById(int id) {
        for (Users user : userslist) {
            if (user.getId() == id) {
                return user;
            }
        }
        throw new IllegalArgumentException("User not found");
    }

    public Users login(String username, String password) {
        for (Users user : userslist) {
            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new IllegalArgumentException("Invalid username or password");
    }

    public String deleteServices(int id) {
        for (Users user : userslist) {
            if (user.getId() == id) {
                userslist.remove(user);
                return "Successfully deleted user";
            }
        }
        throw new IllegalArgumentException("User not found");
    }

    public String deleteCourse(int id) {
        for (Course course : courseslist) {
            if (course.getId() == id) {
                courseslist.remove(course);
                return "Successfully deleted course";
            }
        }
        throw new IllegalArgumentException("Course not found");
    }

    public Users updateServices(int id, Users user) {
        for (Users existingUser : userslist) {
            if (existingUser.getId() == id) {
                existingUser.setUsername(user.getUsername());
                existingUser.setPassword(user.getPassword());
                existingUser.setEmail(user.getEmail());
                return existingUser;
            }
        }
        throw new IllegalArgumentException("User ID not found");
    }

    public Course updateCourses(int id, Course course) {
        for (Course existingCourse : courseslist) {
            if (existingCourse.getId() == id) {
                existingCourse.setName(course.getName());
                existingCourse.setDescription(course.getDescription());
                return existingCourse;
            }
        }
        throw new IllegalArgumentException("Course ID not found");
    }

    public List<Users> getAllUsers() {
        return userslist;
    }

    public List<Course> getAllCourses() {
        return courseslist;
    }

    public boolean addMediaFile(int courseId, String mediaFile) {
        for (Course course : courseslist) {
            if (course.getId() == courseId) {
                course.addMediaFile(mediaFile);
                return true;
            }
        }
        return false;
    }

    public boolean addLesson(int courseId, Lesson lesson) {
        for (Course course : courseslist) {
            if (course.getId() == courseId) {
                lesson.setId(lessonCounter++);
                course.addLesson(lesson);
                return true;
            }
        }
        return false;
    }

    public boolean attendLesson(int userId, int courseId, int lessonId, String OTP) {
        Users user = getUserById(userId);
        Course course = getCourseById(courseId);
        for(Lesson lesson : course.getLessons()){
            if (lesson.getId() == lessonId) {
                lesson.attendStudent(user);
                return true;
            }
        }
        return false;
    }

    public List<Lesson> getAllLessons (int courseId){
        return getCourseById(courseId).getLessons();
    }
}
