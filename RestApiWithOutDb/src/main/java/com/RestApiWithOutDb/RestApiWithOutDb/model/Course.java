package com.RestApiWithOutDb.RestApiWithOutDb.model;

import java.util.*;

import javax.print.attribute.standard.Media;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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


@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Course course = (Course) o;
    return Objects.equals(id, course.id);
}

@Override
public int hashCode() {
    return Objects.hash(id);
}

    public Course(Integer id, String name, String description, String duration, List<String> mediaFiles, List<Lesson> lessons, Set<Users> students, Users instructor) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.students = students;
        this.mediaFiles = mediaFiles;
        this.lessons = lessons;
        this.instructor = instructor; // Ensure instructor is set properly
    }

    public Course() {
        this.mediaFiles = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.students = new HashSet<>();
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
