package com.RestApiWithOutDb.RestApiWithOutDb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;
@Data
@Getter
@Setter
@AllArgsConstructor
public class Quiz {
    private Integer id;
    private String title;
    private Course course;
    private List<Question> questions;  
    private List<StudentQuizScore> studentScores;

    public Quiz(){
        this.studentScores = new ArrayList<>();
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public List<StudentQuizScore> getStudentScores() { return studentScores; }
    public void setStudentScores(List<StudentQuizScore> studentScores) { this.studentScores = studentScores; }

}
