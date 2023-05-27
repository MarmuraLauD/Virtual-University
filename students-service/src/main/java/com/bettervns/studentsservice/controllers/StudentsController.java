package com.bettervns.studentsservice.controllers;

import com.bettervns.studentsservice.dao.StudentDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class StudentsController {

    private final StudentDAO studentDAO;

    @Autowired
    public StudentsController(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @GetMapping("/students")
    public ResponseEntity<?> index(){
        System.out.println(new Gson().toJson(studentDAO.getAllStudents()));
        return ResponseEntity.ok(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(studentDAO.getAllStudents()));
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<?> show(@PathVariable("id") int id){
        return ResponseEntity.ok(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(studentDAO.getStudentById(id)));
    }

    /*@GetMapping("/students/group/{id}")
    public ResponseEntity<?> showStudentsOfGroup(@PathVariable("id") int id) {
        return ResponseEntity.ok(new Gson().toJson(studentDAO.showOfGroup(id)));
    }*/
}