package com.bettervns.adminservice.controllers;

import com.bettervns.adminservice.dao.AdminDAO;
import com.bettervns.adminservice.requests.NewUserRequest;
import com.google.gson.GsonBuilder;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final String STUDENTS_QUEUE_KEY = "studentsQueue";
    private static final String TEACHERS_QUEUE_KEY = "teachersQueue";
    private static final String DIRECT_EXCHANGE_NAME = "betterVNS-direct-exchange";

    private final RabbitTemplate template;
    private final AdminDAO adminDao;

    @Autowired
    public AdminController(RabbitTemplate rabbitTemplate, AdminDAO adminDao) {
        this.template = rabbitTemplate;
        this.adminDao = adminDao;
    }

    @GetMapping("/{id}")
    public String homePage(@PathVariable("id") int id, Model model) {
        model.addAttribute("admin", adminDao.show(id));
        return adminDao.show(id).toString();
    }

    @GetMapping("/students")
    public String getStudentsList(){
        return "not yet implemented";
    }

    @GetMapping("/groups")
    public String getGroupsList(){
        return "not yet implemented";
    }

    @GetMapping("/student/{id}")
    public String getStudent(@PathVariable("id") int id){
        return "not yet implemented";
    }

    @GetMapping("/group/{id}")
    public String getGroup(@PathVariable("id") int id){
        return "not yet implemented";
    }

    @PostMapping("/student")
    public String createStudent(@RequestBody NewUserRequest requestObject){
        System.out.println(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject));
        String message = "create " + 0 + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDENTS_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @PostMapping("/teacher")
    public String createTeacher(@RequestBody NewUserRequest requestObject){
        String message = "create " + 0 + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(TEACHERS_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @DeleteMapping ("/student/{id}")
    public String deleteStudent(@PathVariable("id") int id){
        String message = new String("delete " + id);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDENTS_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @PatchMapping("/student/{id}")
    public String updateStudent(@RequestBody NewUserRequest requestObject, @PathVariable("id") int id){
        String message = "update " + id + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDENTS_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @PostMapping("/group")
    public String createGroup(@RequestBody NewUserRequest requestObject){
        return "not yet implemented";
    }

    @DeleteMapping ("/group/{id}")
    public String deleteGroup(@PathVariable("id") int id){
        return "not yet implemented";
    }

    @PatchMapping("/group/{id}")
    public String updateGroup(@RequestBody NewUserRequest requestObject, @PathVariable("id") int id) {
        return "not yet implemented";
    }
}