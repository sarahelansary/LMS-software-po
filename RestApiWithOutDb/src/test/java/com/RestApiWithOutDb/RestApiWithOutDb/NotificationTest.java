package com.RestApiWithOutDb.RestApiWithOutDb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.RestApiWithOutDb.RestApiWithOutDb.controller.RestControllers;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Notification;
import com.RestApiWithOutDb.RestApiWithOutDb.service.NotificationService;

public class NotificationTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private RestControllers restControllers;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendNotification() {
        String message = "Test message";
        String recipientUsername = "testUser";
        int messageId = 1;

        when(notificationService.addNotification(message, recipientUsername)).thenReturn(messageId);

        String response = restControllers.sendNotification(message, recipientUsername);

        assertEquals("Notification sent successfully! Message ID: " + messageId, response);
         verify(notificationService, times(1)).addNotification(message, recipientUsername);
    }

    @Test
    public void testGetUserNotifications() {
        String username = "testUser";
        boolean unreadOnly = false;
        List<Notification> notifications = List.of(new Notification(null, "Test message", unreadOnly, "testUser"));

        when(notificationService.getNotificationsForUser(username, unreadOnly)).thenReturn(notifications);

        List<Notification> response = restControllers.getUserNotifications(username, unreadOnly);

        assertEquals(notifications, response);
        verify(notificationService, times(1)).getNotificationsForUser(username, unreadOnly);
    }

    @Test
    public void testMarkNotificationAsRead() {
        Integer id = 1;

        doNothing().when(notificationService).markNotificationAsRead(id);

        ResponseEntity<String> response = restControllers.markNotificationAsRead(id);

        assertEquals(ResponseEntity.ok("Notification marked as read."), response);
        verify(notificationService, times(1)).markNotificationAsRead(id);
    }
    @Test
    public void testSendNotificationToStudentAfterCourseAddition() {
        String message = "You have been added to the course.";
        String studentUsername = "studentUser";
        int messageId = 2;

        when(notificationService.addNotification(message, studentUsername)).thenReturn(messageId);

        String response = restControllers.sendNotification(message, studentUsername);

        assertEquals("Notification sent successfully! Message ID: " + messageId, response);
        verify(notificationService, times(1)).addNotification(message, studentUsername);
    }
    @Test
    public void testSendNotificationToStudentAfterInstructorAction() {
        String message = "A new lesson has been added to your course.";
        String studentUsername = "studentUser";
        int messageId = 3;

        when(notificationService.addNotification(message, studentUsername)).thenReturn(messageId);

        String response = restControllers.sendNotification(message, studentUsername);

        assertEquals("Notification sent successfully! Message ID: " + messageId, response);
        verify(notificationService, times(1)).addNotification(message, studentUsername);

        message = "New media has been added to your course.";
        messageId = 4;

        when(notificationService.addNotification(message, studentUsername)).thenReturn(messageId);

        response = restControllers.sendNotification(message, studentUsername);

        assertEquals("Notification sent successfully! Message ID: " + messageId, response);
        verify(notificationService, times(1)).addNotification(message, studentUsername);

        message = "Your course has been deleted.";
        messageId = 5;

        when(notificationService.addNotification(message, studentUsername)).thenReturn(messageId);

        response = restControllers.sendNotification(message, studentUsername);

        assertEquals("Notification sent successfully! Message ID: " + messageId, response);
        verify(notificationService, times(1)).addNotification(message, studentUsername);
    }
    @Test
    public void testSendNotificationToInstructorAfterStudentAttendance() {
        String message = "A student has attended your class.";
        String instructorUsername = "instructorUser";
        int messageId = 6;

        when(notificationService.addNotification(message, instructorUsername)).thenReturn(messageId);

        String response = restControllers.sendNotification(message, instructorUsername);

        assertEquals("Notification sent successfully! Message ID: " + messageId, response);
        verify(notificationService, times(1)).addNotification(message, instructorUsername);
    }
}