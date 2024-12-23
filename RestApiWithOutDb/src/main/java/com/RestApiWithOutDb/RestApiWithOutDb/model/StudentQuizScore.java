package com.RestApiWithOutDb.RestApiWithOutDb.model;

public class StudentQuizScore {
    private Users student;
    private Integer score;
    private String feedback;
    

    
    public Users getStudent() { return student; }
    public void setStudent(Users student) { this.student = student; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public String getfeedback(){return this.feedback = feedback;}
    public void setFeedback(String feedback){;}
}
