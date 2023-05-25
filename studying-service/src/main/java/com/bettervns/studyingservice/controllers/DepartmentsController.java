package com.bettervns.studyingservice.controllers;

import com.bettervns.studyingservice.dao.DepartmentDAO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class DepartmentsController {
    private final DepartmentDAO departmentDAO;

    @Autowired
    public DepartmentsController(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    @GetMapping("/departments")
    public ResponseEntity<?> getAllDepartments() {
        return ResponseEntity.ok(new Gson().toJson(departmentDAO.getAllDepartments()));
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable("id") int id) {
        System.out.println(departmentDAO.getDepartmentById(id));
        return ResponseEntity.ok(new Gson().toJson(departmentDAO.getDepartmentById(id)));
    }
}