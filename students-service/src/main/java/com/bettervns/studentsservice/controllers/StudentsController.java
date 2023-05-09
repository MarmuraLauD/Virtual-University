package com.bettervns.studentsservice.controllers;

import com.bettervns.studentsservice.dao.StudentDAO;
import com.bettervns.studentsservice.models.Student;
import com.bettervns.studentsservice.requests.NewUserRequest;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.Date;

@RestController
@RequestMapping("/students")
public class StudentsController {

    private final StudentDAO studentDAO;

    @Autowired
    public StudentsController(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @GetMapping()
    public ResponseEntity<?> index(){
        return ResponseEntity.ok(new Gson().toJson(studentDAO.index()));
    }

    @PostMapping()
    public String createStudent(@RequestBody NewUserRequest requestObject){
        studentDAO.addStudent(new Student(requestObject.name(), requestObject.surname(), requestObject.father(),
                Date.valueOf(requestObject.date()), requestObject.email(), Integer.parseInt(requestObject.groupId())));
        return "redirect:/students";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        if (studentDAO.show(id) != null) {
            model.addAttribute("student", studentDAO.show(id));
            return "show";
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    @PatchMapping ("/{id}")
    public String update(@ModelAttribute("student") Student student, BindingResult bindingResult, @PathVariable("id") int id){
        if (bindingResult.hasErrors()) return "edit";
        studentDAO.update(id, student);
        return "redirect:/students";
    }

    @DeleteMapping ("/{id}")
    public RedirectView delete(@PathVariable("id") int id){
        studentDAO.delete(id);
        //return "redirect:/students";
        return new RedirectView("http://localhost:8765/students");
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<String> getStudentsOfGroup(@PathVariable("id") int id) {
        return ResponseEntity.ok(new Gson().toJson(studentDAO.showOfGroup(id)));
    }
}