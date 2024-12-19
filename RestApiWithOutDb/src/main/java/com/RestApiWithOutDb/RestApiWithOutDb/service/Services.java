package com.RestApiWithOutDb.RestApiWithOutDb.service;
import com.RestApiWithOutDb.RestApiWithOutDb.service.NotificationService;

import com.RestApiWithOutDb.RestApiWithOutDb.model.Course;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Lesson;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Users;
import org.springframework.stereotype.Component;
// import com.RestApiWithOutDb.RestApiWithOutDb.model.Media; // Removed because Media class does not exist
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.print.attribute.standard.Media;

@Component
public class Services {
    private static int idCounter = 1;
    private static int lessonCounter = 1;
    private List<Users> userslist = new ArrayList<>();
    private List<Course> courseslist = new ArrayList<>();
    @Autowired
    private NotificationService notificationService;

    public Services(NotificationService notificationService) {
        this.notificationService = notificationService;

        Users emily = new Users(idCounter++, "emily", "password123", "instructor", "emily@example.com", new HashSet<>());
        userslist.add(emily);

        Course mathCourse = new Course(idCounter++, "Mathematics", "A math course", "36 hours", new ArrayList<>(), new ArrayList<>(), new HashSet<>(), emily);
        courseslist.add(mathCourse);

        userslist.add(new Users(idCounter++, "john_doe", "password123", "student", "john@example.com", new HashSet<>()));
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

    public String addUserToCourse( Integer userId,Integer courseId) {
        Course course = getCourseById(courseId);
        Users user = getUserById(userId);

        if (course != null && user != null) {
            if (user.getRole().equals("instructor")) {
                // Assign the user as the instructor of the course
                course.setInstructor(user);

                // Notify the instructor
                notificationService.addNotification(
                        "You have been assigned as the instructor for the course: " + course.getName(),
                        user.getUsername());
            } else if (user.getRole().equals("student")) {
                // Add the user as a student to the course
                course.getStudents().add(user);

                // Notify the student
                notificationService.addNotification(
                        "You have been added to the course: " + course.getName(),
                        user.getUsername());
            } else {
                return "Invalid user role.";
            }
            return "User successfully added to course.";
        } else {
            return "User or course not found.";
        }
    }

    public Course getCourseById(Integer courseId) {
        return courseslist.stream()
                .filter(course -> course.getId().equals(courseId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
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
            if (lesson.getId() == lessonId && OTP == lesson.getOTP()) {
                lesson.attendStudent(user);
                return true;
            }
        }
        return false;
    }

    public List<Lesson> getAllLessons (int courseId){
        return getCourseById(courseId).getLessons();
    }

    // Method to handle adding new media to a course
    public boolean addMedia(Integer courseId, Media media) {
        Course course = getCourseById(courseId);
        if (course != null) {
            course.addMediaFile(media.toString());
            return true;
        }
        return false;
    }

    // Method to handle student attending a lesson
    public boolean attendLesson(Integer userId, Integer courseId, Integer lessonId, String OTP) {
        Users student = getUserById(userId);
        Lesson lesson = getLessonById(lessonId);
        Course course = getCourseById(courseId);
        if (student != null && lesson != null && course != null) {
            // Logic to verify OTP and mark attendance
            notificationService.addNotification(
                "Student " + student.getUsername() + " attended the lesson: " + lesson.getUsername(),
                course.getInstructor().getUsername()
            );
            return true;
        }
        return false;
    }

    // Method to handle updating a course
    public boolean updateCourse(Integer courseId, Course updatedCourse) {
        Course course = getCourseById(courseId);
        if (course != null) {
            // Logic to update the course
            List<String> studentUsernames = course.getStudents().stream().map(Users::getUsername).collect(Collectors.toList());
            for (String username : studentUsernames) {
                notificationService.addNotification(
                    "The course " + course.getName() + " has been updated.",
                    username
                );
            }
            return true;
        }
        return false;
    }

    // Method to handle deleting a course
    public boolean deleteCourse(Integer courseId) {
        Course course = getCourseById(courseId);
        if (course != null) {
            // Logic to delete the course
            List<String> studentUsernames = course.getStudents().stream().map(Users::getUsername).collect(Collectors.toList());
            for (String username : studentUsernames) {
                notificationService.addNotification(
                    "The course " + course.getName() + " has been deleted.",
                    username
                );
            }
            return true;
        }
        return false;
    }

    // Method to get a lesson by its ID
    public Lesson getLessonById(Integer lessonId) {
        for (Course course : courseslist) {
            for (Lesson lesson : course.getLessons()) {
                if (lesson.getId() == lessonId) {
                    return lesson;
                }
            }
        }
        throw new IllegalArgumentException("Lesson not found");
    }

    // Notify students about new media
    public void notifyStudentsNewMedia(Integer courseId, String mediaName) {
        Course course = getCourseById(courseId);
        if (course != null) {
            List<String> studentUsernames = course.getStudents().stream().map(Users::getUsername).collect(Collectors.toList());
            for (String username : studentUsernames) {
                notificationService.addNotification(
                    "New media added to the course: " + course.getName() + ". Media: " + mediaName,
                    username
                );
            }
        }
    }

    // Notify students about new lesson
    public void notifyStudentsNewLesson(Integer courseId, String lessonName) {
        Course course = getCourseById(courseId);
        if (course != null) {
            List<String> studentUsernames = course.getStudents().stream().map(Users::getUsername).collect(Collectors.toList());
            for (String username : studentUsernames) {
                notificationService.addNotification(
                    "New lesson added to the course: " + course.getName() + ". Lesson: " + lessonName,
                    username
                );
            }
        }
    }

    // Notify instructor about student attendance
    public void notifyInstructorStudentAttendedLesson(Integer studentId, Integer lessonId) {
        Users student = getUserById(studentId);
        Lesson lesson = getLessonById(lessonId);
        Course course = lesson.getCourse();
        if (student != null && lesson != null && course != null) {
            notificationService.addNotification(
                "Student " + student.getUsername() + " attended the lesson: " + lesson.getUsername(),
                course.getInstructor().getUsername()
            );
        }
    }

    // Notify students about course update
    public void notifyStudentsCourseUpdated(Integer courseId) {
        Course course = getCourseById(courseId);
        if (course != null) {
            List<String> studentUsernames = course.getStudents().stream().map(Users::getUsername).collect(Collectors.toList());
            for (String username : studentUsernames) {
                notificationService.addNotification(
                    "The course " + course.getName() + " has been updated.",
                    username
                );
            }
        }
    }

    // Notify students about course deletion
    public void notifyStudentsCourseDeleted(Integer courseId) {
        Course course = getCourseById(courseId);
        if (course != null) {
            List<String> studentUsernames = course.getStudents().stream().map(Users::getUsername).collect(Collectors.toList());
            for (String username : studentUsernames) {
                notificationService.addNotification(
                    "The course " + course.getName() + " has been deleted.",
                    username
                );
            }
        }
    }
}
