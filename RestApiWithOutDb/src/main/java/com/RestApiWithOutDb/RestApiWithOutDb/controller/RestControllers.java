package com.RestApiWithOutDb.RestApiWithOutDb.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.RestApiWithOutDb.RestApiWithOutDb.model.Course;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Users;
import com.RestApiWithOutDb.RestApiWithOutDb.service.Services;

//@org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("api")
public class RestControllers {
    @Autowired
    private Services services;



    @PostMapping("/register")
    public String createStudent(@RequestBody Users user){
       String s =  services.createServices(user);
        return s;
    }

@PostMapping("/login")
public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
    try {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Username and password are required.");
        }

        Users user = services.login(username, password);
        return ResponseEntity.ok("Welcome, " + user.getUsername());
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}


    @GetMapping("/getstudent")
    public Users getStudent(@RequestParam int id){
        return services.getUserById(id);
    }


    @DeleteMapping("/delete")
    public String deleteStudent(@RequestParam int id){
            services.deleteServices(id);
            return "delete Successfully";
    }


    @PostMapping("/update")
    public Users update(@RequestBody Users user){

        return services.updateServices( user.getId(), user );

    }

    @PostMapping("/updateCourse")
    public Course updatec(@RequestBody Course course){

        return services.updateCourses( course.getId(), course );

    }


    @GetMapping("/getAll")
    public List<Users> getAll(){
        return services.getAllUsers();
    }

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
@PostMapping("/createCourse")
public String createCourse(@RequestBody Course course) {
    return services.createCourse(course);
}

@PostMapping("/addUserToCourse")
public String addUserToCourse(@RequestParam Integer userId, @RequestParam Integer courseId) {
    return services.addUserToCourse(userId, courseId);
}

@GetMapping("/getAllCourses")
    public List<Course> getAllcCourses(){
        return services.getAllCourses();
    }


}
