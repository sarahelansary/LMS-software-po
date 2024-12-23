package com.RestApiWithOutDb.RestApiWithOutDb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.RestApiWithOutDb.RestApiWithOutDb.model.Assignment;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Course;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Question;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Quiz;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Users;
import com.RestApiWithOutDb.RestApiWithOutDb.service.Services;

@SpringBootTest
class PerformanceTrackingTest {

    private Services services;

    @BeforeEach
    void setUp() {
        services = new Services(null, null);
    }

    @Test
    void testCreateQuiz() {
        Quiz quiz = new Quiz(0, "Test Quiz", null, new ArrayList<>(), new ArrayList<>());
        String result = services.createQuiz(quiz);
        assertEquals("Quiz successfully created", result);
        assertEquals(1, services.getAllQuizzes().size());
    }

    @Test
    void testCreateRandomizedQuiz() {
        Quiz quiz = new Quiz(null, "Randomized Quiz", null, new ArrayList<>(), new ArrayList<>());
        for (int i = 0; i < 10; i++) {
            Question question = new Question();
            question.setId(i);
            question.setQuestionText("Question" + i);
            question.setType("MCQ");
            question.setOptions(null);
            question.setCorrectAnswer("Answer " + i);
            quiz.getQuestions().add(question);
        }
        services.createQuiz(quiz);

        Quiz randomizedQuiz = services.createRandomizedQuiz(quiz.getId(), 5);
        assertEquals(5, randomizedQuiz.getQuestions().size());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            services.createRandomizedQuiz(quiz.getId(), 20);
        });
        assertEquals("Not enough questions available for the requested random selection.", exception.getMessage());
    }

    @Test
    void testAddQuestionToQuiz() {
        Quiz quiz = new Quiz(null, "Add Question Quiz", null, new ArrayList<>(), new ArrayList<>());
        services.createQuiz(quiz);

        Question question = new Question();
            question.setId(1);
            question.setQuestionText("Sample Question");
            question.setType("MCQ");
            question.setOptions(null);
            question.setCorrectAnswer("Sample Answer");
            quiz.getQuestions().add(question);

        String result = services.addQuestionToQuiz(quiz.getId(), question);
        assertEquals("Question added to quiz", result);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            services.addQuestionToQuiz(999, question);
        });
        assertEquals("Quiz not found", exception.getMessage());
    }

    @Test
    void testSubmitQuizScore() {
        Quiz quiz = new Quiz(null, "Quiz with Score", null, new ArrayList<>(), new ArrayList<>());
        services.createQuiz(quiz);

        Users student = new Users(null, "testStudent", "password", "student", "test@example.com", null);
        String result = services.submitQuizScore(quiz.getId(), student, 95);
        assertEquals("Score successfully recorded", result);
        assertEquals(1, quiz.getStudentScores().size());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            services.submitQuizScore(99, student, 95); // Simulate invalid quiz ID
        });
        assertEquals("Quiz not found", exception.getMessage());
    }

    @Test
    void testGradeAssignment() {
        Course course = new Course(1, "Test Course", "Description", "5 hours", new HashSet<>());
        Users student = new Users(1, "john_doe", "password", "student", "john@example.com", null);

        Assignment assignment = new Assignment();
        assignment.setId(1);
        assignment.setTitle("Test Assignment");
        assignment.setCourse(course);
        assignment.setStudent(student);
        assignment.setFileUrl("file_url.pdf");
        assignment.setFeedback(null);
        assignment.setGrade(null);
        assignment.setSubmissionDate(new Date());

        services.getAllAssignments().add(assignment);
        String result = services.gradeAssignment(1, student, 85, "Good job");
        assertEquals("Assignment graded successfully for student: john_doe", result);

        String notFoundResult = services.gradeAssignment(99, student, 85, "Good job");
        assertEquals("Assignment not found", notFoundResult);
    }
}
