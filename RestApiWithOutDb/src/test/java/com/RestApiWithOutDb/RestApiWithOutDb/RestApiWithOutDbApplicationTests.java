package com.RestApiWithOutDb.RestApiWithOutDb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;

import com.RestApiWithOutDb.RestApiWithOutDb.controller.RestControllers;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Course;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Lesson;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Users;
import com.RestApiWithOutDb.RestApiWithOutDb.service.NotificationService;
import com.RestApiWithOutDb.RestApiWithOutDb.service.Services;


@SpringBootTest
class RestApiWithOutDbApplicationTests {

    private AuthenticationManager authenticationManager;
    private NotificationService notificationService = new NotificationService();
	private Services services = new Services(notificationService);

	private RestControllers restControllers = new RestControllers(services, notificationService, authenticationManager);

	private Course course = new Course(
		0,
		"testCourse", 
		"testDescription", 
		"0 hours", 
		new HashSet<>()
	);
	private Users student = new Users(
		0,
		"testUser", 
		"testpass", 
		"student",
		"test@gmail.com",
		new HashSet<>()
	);
	private Lesson lesson = new Lesson(
		0,
		"testLesson",
		"testlesson.pdf",
		"testotp"
	);
	
	@Test
	void contextLoads() {
	}

	// testing course creation
	@Test
	void creatCourseTest() {
		assertEquals("Course successfully created", restControllers.createCourse(course));
	}

	// testing course update
	@Test
	void updateCourseTest() {
		restControllers.createCourse(course);
		Course updatedCourse = new Course(
			course.getId(),
			"upDatedTestCourse",
			"upDatedtestDescription",
			"0 hours",
			new HashSet<>()
		);


		restControllers.updateCourse(updatedCourse);
		assertEquals(updatedCourse, restControllers.updateCourse(updatedCourse));

		Course updatedCourse2 = new Course(
			-1,
			"upDatedTestCourse",
			"upDatedtestDescription",
			"10 hours",
			new HashSet<>()
		);
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			restControllers.updateCourse(updatedCourse2);
		});
		assertEquals("Course ID not found", exception.getMessage());
	}

	// testing enrollment
	@Test
	void addUserToCourseTest() {
		restControllers.createCourse(course);
		restControllers.registerUser(student);
		assertEquals("User successfully added to course",
			restControllers.addUserToCourse(student.getId(), course.getId()));

		IllegalArgumentException courseException = assertThrows(IllegalArgumentException.class, () -> {
			restControllers.addUserToCourse(student.getId(), -1);
		});
		assertEquals("Course not found", courseException.getMessage());

		IllegalArgumentException userExeption = assertThrows(IllegalArgumentException.class, () -> {
			restControllers.addUserToCourse(-1, course.getId());
		});
		assertEquals("User not found", userExeption.getMessage());
	}

	// testin course deletion
	@Test
	void deleteCourseTest() {
		restControllers.createCourse(course);
		assertEquals("delete Successfully",
			restControllers.deleteCourse(course.getId()));

		IllegalArgumentException courseException = assertThrows(IllegalArgumentException.class, () -> {
			restControllers.deleteCourse(-1);
		});
		assertEquals("Course not found", courseException.getMessage());
	}

	//tesing media file adding
	@Test
	void addMediaFileTest() {
		restControllers.createCourse(course);
		assertEquals("Media added Successfully",
			restControllers.addMediaFile(course.getId(), "testMedia.pdf"));

		assertEquals("The course is not exist!",
			restControllers.addMediaFile(-1, "testMedia.pdf"));
	}

	//tesing lesson adding
	@Test
	void addLessonTest() {
		restControllers.createCourse(course);
		assertEquals("Lesson added Successfully",
			restControllers.addLesson(course.getId(), lesson));

		assertEquals("The course is not exist!",
			restControllers.addLesson(-1, lesson));
	}

	// testing attendance
	@Test
	void attendStudentTest() {
		restControllers.createCourse(course);
		restControllers.registerUser(student);
		restControllers.addLesson(course.getId(), lesson);

		assertEquals("Student attended Successfully",
			restControllers.attendStudent(student.getId(), course.getId(), lesson.getId(), "testotp"));

		assertEquals("The lesson is not exist or wrong Password!",
			restControllers.attendStudent(student.getId(), course.getId(), lesson.getId(), "testotp1"));
			
		assertEquals("The lesson is not exist or wrong Password!",
			restControllers.attendStudent(student.getId(), course.getId(), -1, "testotp"));
		
		IllegalArgumentException courseException = assertThrows(IllegalArgumentException.class, () -> {
			restControllers.attendStudent(student.getId(), -1, lesson.getId(), "testotp");
		});
		assertEquals("Course not found", courseException.getMessage());

		IllegalArgumentException userExeption = assertThrows(IllegalArgumentException.class, () -> {
			restControllers.attendStudent(-1, course.getId(), lesson.getId(), "testotp1");
		});
		assertEquals("User not found", userExeption.getMessage());
	}

}
