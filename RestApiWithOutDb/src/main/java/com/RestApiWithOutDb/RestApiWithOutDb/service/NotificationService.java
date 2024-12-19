package com.RestApiWithOutDb.RestApiWithOutDb.service;

import com.RestApiWithOutDb.RestApiWithOutDb.model.Notification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private static int idCounter = 1;
    private final List<Notification> notifications = new ArrayList<>();

    // Add a new notification
    public int addNotification(String message, String recipientUsername) {
        Notification notification = new Notification(idCounter++, message, false, recipientUsername);
        notifications.add(notification);
        return notification.getId(); // Return the ID of the newly added notification
    }

    // Fetch all notifications for a specific user
    public List<Notification> getNotificationsForUser(String username, boolean onlyUnread) {
        return notifications.stream()
                .filter(n -> n.getRecipientUsername().equals(username))
                .filter(n -> !onlyUnread || !n.isRead())
                .collect(Collectors.toList());
    }

    // Mark notifications as read
    public void markNotificationAsRead(Integer id) {
        notifications.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .ifPresent(n -> n.setRead(true));
    }
}