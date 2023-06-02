package com.bettervns.studyingservice.controllers;

import com.bettervns.studyingservice.dao.GroupDAO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studying")
public class GroupsController {

    private final GroupDAO groupDAO;

    @Autowired
    public GroupsController(GroupDAO groupDAO ) {
        this.groupDAO = groupDAO;
    }

    @GetMapping("/groups")
    public ResponseEntity<?> index(){
        return ResponseEntity.ok(new Gson().toJson(groupDAO.getAllGroups()));
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<?> show(@PathVariable("id") int id){
        return ResponseEntity.ok(new Gson().toJson(groupDAO.getGroupById(id)));
    }

    @GetMapping("/groups/{departmentId}")
    public ResponseEntity<?> getGroupsByDepartmentId(@PathVariable("departmentId") int departmentId){
        return ResponseEntity.ok(new Gson().toJson(groupDAO.getGroupsByDepartmentId(departmentId)));
    }
}