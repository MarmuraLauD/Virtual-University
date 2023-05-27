package com.bettervns.teachersservice.controllers;


import com.bettervns.teachersservice.dao.TeacherDAO;
import com.bettervns.teachersservice.models.Teacher;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

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

//    @GetMapping("/teacher/{id}")
//    public ResponseEntity<Teacher> getTeacherById(@PathVariable("id") int employeeId){
//        return new ResponseEntity<Teacher>(teacherDAO.getTeacherById(employeeId), HttpStatus.OK);
//    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable("id") int id) {
        System.out.println(teacherDAO.getTeacherById(id));
        return ResponseEntity.ok(new Gson().toJson(teacherDAO.getTeacherById(id)));
    }





//    @PostMapping()
//    public String newTeacher(@ModelAttribute("teacher") Teacher teacher, BindingResult bindingResult){
//        if (bindingResult.hasErrors()) return "teachers/new";
//        System.out.println(teacher);
//        teacherDAO.addTeacher(teacher);
//        return "redirect:/teachers";
//    }
//

//    @PatchMapping("/{id}")
//    public String update(@ModelAttribute("teacher") Teacher teacher, BindingResult bindingResult, @PathVariable("id") int id) {
//        System.out.println(teacher.toString());
//        if (bindingResult.hasErrors()) return "edit";
//        teacherDAO.update(id, teacher);
//        return "redirect:/teachers";
//    }
//
//
//
//    @DeleteMapping("/{id}")
//    public RedirectView delete(@PathVariable("id") int id) {
//        teacherDAO.deleteTeacher(id);
//        return new RedirectView("http://localhost:8080/teachers");
//    }
//
}
