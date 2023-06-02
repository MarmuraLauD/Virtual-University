package com.bettervns.teachersservice.controllers;

import com.bettervns.teachersservice.dao.TeacherDAO;
import com.bettervns.teachersservice.models.Teacher;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping()
public class TeachersController {

    private final TeacherDAO teacherDAO;

    @Autowired
    public TeachersController(TeacherDAO teacherDAO) {
        this.teacherDAO = teacherDAO;
    }

    @GetMapping("/teachers")
    public ResponseEntity<?> getAllTeachers() {
        return ResponseEntity.ok(new Gson().toJson(teacherDAO.getAllTeachers()));
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable("id") int id) {
        System.out.println(teacherDAO.getTeacherById(id));
        return ResponseEntity.ok(new Gson().toJson(teacherDAO.getTeacherById(id)));
    }

}