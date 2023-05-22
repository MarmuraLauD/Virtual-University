package com.bettervns.teachersservice.controllers;


import com.bettervns.teachersservice.dao.TeacherDAO;
import com.bettervns.teachersservice.models.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeachersController {

    private final TeacherDAO teacherDAO;

    @Autowired
    public TeachersController(TeacherDAO teacherDAO) {
        this.teacherDAO = teacherDAO;
    }

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherDAO.getAllTeachers();
    }

    @GetMapping("{id}")
    public ResponseEntity<Teacher> getEmployeeById(@PathVariable("id") int employeeId){
        return new ResponseEntity<Teacher>(teacherDAO.getTeacherById(employeeId), HttpStatus.OK);
    }

    @GetMapping("/new")
    public String newTeacher(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "new";
    }

    @PostMapping()
    public Teacher createTeacher(@RequestBody Teacher teacher) {
        return teacherDAO.addTeacher(teacher);
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("teacher", teacherDAO.getTeacherById(id));
        return "edit";
    }
//
//    @PostMapping()
//    public String newTeacher(@ModelAttribute("teacher") Teacher teacher, BindingResult bindingResult){
//        if (bindingResult.hasErrors()) return "teachers/new";
//        System.out.println(teacher);
//        teacherDAO.addTeacher(teacher);
//        return "redirect:/teachers";
//    }
//

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("teacher") Teacher teacher, BindingResult bindingResult, @PathVariable("id") int id) {
        System.out.println(teacher.toString());
        if (bindingResult.hasErrors()) return "edit";

        teacherDAO.update(id, teacher);
        return "redirect:/teachers";
    }


    @DeleteMapping("/{id}")
    public RedirectView delete(@PathVariable("id") int id) {
        teacherDAO.deleteTeacher(id);
        return new RedirectView("http://localhost:8080/teachers");
    }
//
}
