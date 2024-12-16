package com.RestApiWithOutDb.RestApiWithOutDb.model;

import java.util.Set;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Course {
    private Integer id;
    private String name;
    private String description;
    
   
    private Set<Users> students;

    public Integer getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Set<Users> getStudents() { return students; }
    public void setStudents(Set<Users> students) { this.students = students; }
    

    
}
