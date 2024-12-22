package com.RestApiWithOutDb.RestApiWithOutDb.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Lesson {
    private int id;
    private String title;
    private String content;
    private String OTP;

    private Set<Users> students;

    public Lesson(int id, String title, String content, String oTP) {
        this.id = id;
        this.title = title;
        this.content = content;
        OTP = oTP;
        students = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int lessonNumber) {
        this.id = lessonNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public Set<Users> getStudents() { return students; }
    public void attendStudent(Users student) { this.students.add(student); }

    // public Course getCourse() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getCourse'");
    // }

    // public String getUsername() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getUsername'");
    // }
}
