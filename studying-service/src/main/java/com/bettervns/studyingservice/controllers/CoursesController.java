/*
package com.bettervns.studyingservice.controllers;

import com.bettervns.studyingservice.dao.CourseDAO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/studying")
public class CoursesController {
    private final CourseDAO courseDAO;

    @Autowired
    public CoursesController(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @GetMapping("/courses")
    public ResponseEntity<?> index(){
        return ResponseEntity.ok(new Gson().toJson(courseDAO.index()));
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<?> show(@PathVariable("id") int id){
        return ResponseEntity.ok(new Gson().toJson(courseDAO.show(id)));
    }
}
*/
