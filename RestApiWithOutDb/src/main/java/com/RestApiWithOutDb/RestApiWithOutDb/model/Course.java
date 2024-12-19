package com.RestApiWithOutDb.RestApiWithOutDb.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.print.attribute.standard.Media;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
//@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Course {
    
    private Integer id;
    private String name;
    private String description;
    private String duration;

    private List<String> mediaFiles;
    private List<Lesson> lessons;
   
    private Set<Users> students;
    private Users instructor;

    public Course() {
    }


    public Course(Integer id, String name, String description, String duration, Set<Users> students, Users instructor) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.students = students;
        this.instructor = instructor;

        this.mediaFiles = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }

    public Course() {
        this.mediaFiles = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.students = new HashSet<>();
    }
    public Course(int i, String string, String string2, Object object) {
        //TODO Auto-generated constructor stub
    }
 
    public Course(int id, String name, String description, String duration, Set<Users> students) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.students = new HashSet<>();
        this.lessons = new ArrayList<>();
        this.mediaFiles = new ArrayList<>();
    
     
        
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Set<Users> getStudents() { return students; }
    public void setStudents(Set<Users> students) { this.students = students; }

    public List<String> getMediaFiles() { return mediaFiles; }
    public void addMediaFile(String media) { this.mediaFiles.add(media); }
    public List<Lesson> getLessons() { return lessons; }
    public void addLesson(Lesson lesson) { this.lessons.add(lesson); }
    
    public Users getInstructor() { return instructor; }
    public void setInstructor(Users instructor) { this.instructor = instructor; }


    
}
