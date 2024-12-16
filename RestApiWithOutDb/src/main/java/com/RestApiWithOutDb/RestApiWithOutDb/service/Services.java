package com.RestApiWithOutDb.RestApiWithOutDb.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.RestApiWithOutDb.RestApiWithOutDb.model.Course;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Users;


@Component
public class Services {
    private static int idCounter = 1;
     private List<Users> userslist = new ArrayList<>();
    private List<Course> courseslist = new ArrayList<>();
;


    public Services( ) {

        courseslist.add( new Course(idCounter++, "Mathematics", "A math course", new HashSet<>()));
       userslist.add(new Users(idCounter++, "john_doe", "password123", "student", "john@example.com",new HashSet<>()));
    }

    public String hello(){
        return ("<h1> sucess</h1>");}


    public String createServices (Users user) {
        Optional<Users> existingUser = userslist.stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findFirst();

        if (existingUser.isPresent()) {
            return "Email already exists!";
        }

        user.setId(idCounter++);

        userslist.add(user);
        return "User successfully registered with ID: " + user.getId();
    }

    // Authenticate user
//    public Users login(String email, String password) {
//        Users user = userslist.stream()
//                .filter(u -> u.getEmail().equals(email))
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
//
//        if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
//                return user;
//
//        } else {
//            throw new IllegalArgumentException("Invalid email or password");
//        }
//    }


     // create course
     public String createCourse(Course course) {
        course.setId(idCounter++);
        courseslist.add(course);
        return "Course successfully created";
    }


    public String addUserToCourse(Integer userId, Integer courseId) {
        Users user = getUserById(userId);
        Course course = getCourseById(courseId);

        if (user != null && course != null) {
            course.getStudents().add(user);  // Add user to course
            user.getCourses().add(course);    // Add course to user
            return "User successfully added to course";
        } else {
            return "User or course not found";
        }
    }

    public Course getCourseById(Integer courseId) {
        for(Course s : courseslist){
            if(s.getId() == courseId){
                return s;
            }
        }
    throw new IllegalArgumentException("Student not found");
    }

//get the student (view information)
    public Users getUserById(int id){
        for(Users s : userslist){
            if(s.getId() == id){
                return s;
            }
        }
    throw new IllegalArgumentException("Student not found");
    }

//login
    public Users login(String username, String password) {
        for (Users s : userslist) {
            if (s.getUsername().equalsIgnoreCase(username) && s.getPassword().equals(password)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid username or password");
    }


    //delete the student
    public String deleteServices(int id)
    {
        for(Users s : userslist){
            if(s.getId()==id){
                userslist.remove(s);
                return "Successfully deleted student";
            }
        }
        throw new IllegalArgumentException("Student not found");


    }

    public String deleteCourse(int id)
    {
        for(Course s : courseslist){
            if(s.getId()==id){
                courseslist.remove(s);
                return "Successfully deleted course";
            }
        }
        throw new IllegalArgumentException("course not found");


    }


    //update

    public Users updateServices(int id, Users user){

        for(Users s : userslist){
            if(s.getId()==id){
                s.setUsername(user.getUsername());
                s.setPassword(user.getPassword());
                s.setEmail(user.getEmail());
                return s;
            }
        }
        throw new IllegalArgumentException("id not found");

    }

    public Course updateCourses(int id, Course course){

        for(Course s : courseslist){
            if(s.getId()==id){
                s.setName(course.getName());
                s.setDescription(course.getDescription());
                return s;
            }
        }
        throw new IllegalArgumentException("id not found");

    }

public List<Users> getAllUsers()
{
    return userslist;
}
public List<Course> getAllCourses()
{
    return courseslist;
}

}
