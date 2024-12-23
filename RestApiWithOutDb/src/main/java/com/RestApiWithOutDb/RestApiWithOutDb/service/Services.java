package com.RestApiWithOutDb.RestApiWithOutDb.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.RestApiWithOutDb.RestApiWithOutDb.model.Assignment;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Attendance;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Course;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Lesson;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Question;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Quiz;
import com.RestApiWithOutDb.RestApiWithOutDb.model.StudentQuizScore;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Users;

@Component
public class Services {
    private  int idCounter = 1;
    private  int idCourseCounter = 1;
    private static int lessonCounter = 1;
    private static int assignmentidCounter = 1;
    private List<Users> userslist = new ArrayList<>();
    private List<Course> courseslist = new ArrayList<>();
    private List<Quiz> quizList = new ArrayList<>();

    private List<Assignment> assignmentList = new ArrayList<>();
    private List<Attendance> attendanceList = new ArrayList<>();

    private NotificationService notificationService;
    private EmailService emailService;

    public Services(NotificationService notificationService, EmailService emailservice) {
        this.notificationService = notificationService;
        this.emailService = emailservice;
        // Adding default courses
        courseslist.add(new Course(idCourseCounter++, "Mathematics", "A math course", "36 hours", new HashSet<>()));

        // Adding default users
        userslist.add(new Users(idCounter++, "john_doe", "password123", "student", "john@example.com",
                new HashSet<Integer>()));
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

        if (user != null && course != null && "student".equalsIgnoreCase(user.getRole())) {
            course.getStudents().add(user.getId());
            user.getCourses().add(course.getId());

            // Add a notification
            notificationService.addNotification(
                    "You have been added to the course: " + course.getName(),
                    user.getUsername());
            emailService.sendEmail(user.getEmail(), "Course Enrollment", "You have been enrolled in the course.");
            return "User successfully added to course";
        } else {
            return "User or course not found";
        }

    }

    public String removeUserFromCourse(Integer userId, Integer courseId) {
        Users user = getUserById(userId);
        Course course = getCourseById(courseId);

        if (user != null && course != null && course.getStudents().contains(userId)) {
            course.getStudents().remove(userId);
            user.getCourses().remove(courseId);

            notificationService.addNotification(
                    "You have been removed from the course: " + course.getName(),
                    user.getUsername());
            emailService.sendEmail(user.getEmail(), "Course Enrollment", "You have been removed from the course.");
            return "User successfully removed from course";
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
        for (Lesson lesson : course.getLessons()) {
            if (lesson.getId() == lessonId && lesson.getOTP().equals(OTP)) {
                lesson.attendStudent(user);
                return true;
            }
        }
        return false;
    }

    public List<Lesson> getAllLessons(int courseId) {
        return getCourseById(courseId).getLessons();
    }

    // Create quiz
    public String createQuiz(Quiz quiz) {
        quiz.setId(idCounter++);
        quizList.add(quiz);
        return "Quiz successfully created";
    }

    public Quiz createRandomizedQuiz(Integer quizId, int numberOfRandomQuestions) {
        
        Quiz quiz = getQuizById(quizId);
        List<Question> questions = quiz.getQuestions();
        // Ensure there are enough questions to select from
        if (questions.size() < numberOfRandomQuestions) {
            throw new IllegalArgumentException("Not enough questions available for the requested random selection.");
        }
        
        List<Question> randomQuestions = new ArrayList<>();
        Random random = new Random();
        
        List<Integer> selectedIndexes = new ArrayList<>();
        
        while (randomQuestions.size() < numberOfRandomQuestions) {
            int randomIndex = random.nextInt(questions.size());  // Generate a random index
            
            
            if (!selectedIndexes.contains(randomIndex)) {
                randomQuestions.add(questions.get(randomIndex));  
                selectedIndexes.add(randomIndex);  
            }
        }
        quiz.setQuestions(randomQuestions);
         return quiz;
    }

    //addQuestionToQuiz
    public String addQuestionToQuiz(Integer quizId, Question question) {
        Quiz quiz = getQuizById(quizId); 
        if (quiz != null) {
            quiz.getQuestions().add(question);  
            return "Question added to quiz";
        }
        return "Quiz not found";
    }

    // Get quiz by ID
    public Quiz getQuizById(Integer quizId) {
        for (Quiz quiz : quizList) {
            if (quiz.getId().equals(quizId)) {
                return quiz;
            }
        }
        throw new IllegalArgumentException("Quiz not found");
    }
//Feedback
    private String generateFeedback(Integer score) {
    if (score >= 90) {
        return "Excellent work!";
    } else if (score >= 75) {
        return "Good job";
    } else if (score >= 50) {
        return "You passed";
    } else {
        return "You did not pass.";
    }
}

    // quiz score
    public String submitQuizScore(Integer quizId, Users student, Integer score) {
        Quiz quiz = getQuizById(quizId);
        if (quiz != null) {
            StudentQuizScore studentScore = new StudentQuizScore();
            studentScore.setStudent(student);
            studentScore.setScore(score);
            String feedback = generateFeedback(score);
            studentScore.setFeedback(feedback);
            quiz.getStudentScores().add(studentScore);
            return "Score successfully recorded";
        }
        return "Quiz not found";
    }

    // Submit assignment
    public String submitAssignment(Integer studentId, Integer courseId, String fileUrl) {
        Users student = getUserById(studentId);
        Course course = getCourseById(courseId);

        if (student != null && course != null) {
            Assignment assignment = new Assignment();
            assignment.setId(assignmentidCounter++); 
            assignment.setStudent(student);
            assignment.setCourse(course);
            assignment.setFileUrl(fileUrl);
            assignment.setSubmissionDate(new java.util.Date());  
            assignmentList.add(assignment);
            return "Assignment submitted successfully";
        }
        return "Student or Course not found";
    }
    

    // Grade assignment
    public String gradeAssignment(Integer assignmentId, Users student, Integer grade, String feedback) {
 
    Assignment assignment = getAssignmentById(assignmentId);
    if (assignment != null) {

        assignment.setGrade(grade);
        assignment.setFeedback(feedback);

        return "Assignment graded successfully for student: " + student.getUsername();
    }
    return "Assignment not found";
}


    // Get assignment by ID
    public Assignment getAssignmentById(Integer assignmentId) {
        if (assignmentId == null) {
            return null;  
        }
        for (Assignment assignment : assignmentList) {
            if (assignment.getId() != null && assignment.getId().equals(assignmentId)) {
                return assignment;  
            }
        }
        return null;  
    }

    // Track attendance
    public String markAttendance(Integer studentId, Integer courseId, Boolean isPresent) {
        Users student = getUserById(studentId);
        Course course = getCourseById(courseId);

        if (student != null && course != null) {
            Attendance attendance = new Attendance();
            attendance.setStudent(student);
            attendance.setCourse(course);
            attendance.setIsPresent(isPresent);
            attendance.setAttendanceDate(new java.util.Date());  
            attendanceList.add(attendance);
            return "Attendance marked successfully";
        }
        return "Student or Course not found";
    }

   
    public List<Quiz> getAllQuizzes() {
        return quizList;  
    }

    public List<Assignment> getAllAssignments() {
        return assignmentList;  
    }

    public List<Attendance> getAllAttendance() {
        return attendanceList;
    }
    
    public Users getUserById2(int id) {
        return new Users(id, "john_doe", "password123", "student", "john@example.com", null);
    }

}
