package com.RestApiWithOutDb.RestApiWithOutDb.model;

import java.time.LocalDateTime;

public class Notification {
    private Integer id;
    private String message;
    private boolean isRead;
    private String recipientUsername; // Username of the user receiving the notification
    private LocalDateTime timestamp;

    public Notification(Integer id, String message, boolean isRead, String recipientUsername) {
        this.id = id;
        this.message = message;
        this.isRead = isRead;
        this.recipientUsername = recipientUsername;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public Integer setIdN(Integer id) {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }
    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}