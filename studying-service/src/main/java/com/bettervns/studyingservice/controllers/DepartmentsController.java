package com.bettervns.studyingservice.controllers;

import com.bettervns.studyingservice.dao.DepartmentDAO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/studying")
public class DepartmentsController {
    private final DepartmentDAO departmentDAO;

    @Autowired
    public DepartmentsController(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    @GetMapping("/departments")
    public ResponseEntity<?> index(){
        return ResponseEntity.ok(new Gson().toJson(departmentDAO.index()));
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<?> show(@PathVariable("id") int id){
        return ResponseEntity.ok(new Gson().toJson(departmentDAO.show(id)));
    }
}