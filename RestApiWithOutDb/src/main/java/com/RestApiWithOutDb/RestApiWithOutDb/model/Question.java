package com.RestApiWithOutDb.RestApiWithOutDb.model;

public class Question {
    private Integer id;
    private String questionText;
    private String type;  // Question type: MCQ, True/False, etc.
    private String[] options; // Options for MCQs
    private String correctAnswer;

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String[] getOptions() { return options; }
    public void setOptions(String[] options) { this.options = options; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
}
