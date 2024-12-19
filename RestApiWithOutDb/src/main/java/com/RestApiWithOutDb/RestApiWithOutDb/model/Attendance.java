package com.RestApiWithOutDb.RestApiWithOutDb.model;

import java.util.Date;

public class Attendance {

    private Integer id;
    private Users student;
    private Course course;
    private Date attendanceDate;
    private Boolean isPresent; // true if present, false if absent

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Users getStudent() { return student; }
    public void setStudent(Users student) { this.student = student; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Date getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(Date attendanceDate) { this.attendanceDate = attendanceDate; }
    public Boolean getIsPresent() { return isPresent; }
    public void setIsPresent(Boolean isPresent) { this.isPresent = isPresent; }
    
}
