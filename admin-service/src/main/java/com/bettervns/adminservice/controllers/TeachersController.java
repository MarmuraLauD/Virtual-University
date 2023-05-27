package com.bettervns.adminservice.controllers;

import com.bettervns.adminservice.requests.TeacherRequest;
import com.google.gson.GsonBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/teacher")
public class TeachersController {

    private static final String DIRECT_EXCHANGE_NAME = "betterVNS-direct-exchange";
    private static final String TEACHERS_QUEUE_KEY = "teachersQueue";
    private final RabbitTemplate template;

    @Autowired
    public TeachersController(RabbitTemplate rabbitTemplate) {
        this.template = rabbitTemplate;
    }

    @PostMapping()
    public String createTeacher(@RequestBody TeacherRequest requestObject){
        String message = "create " + "teacher " + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(TEACHERS_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @PatchMapping("/{id}")
    public String updateTeacher(@RequestBody TeacherRequest requestObject, @PathVariable("id") int id){
        String message = "update " + "teacher " + id + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(TEACHERS_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @DeleteMapping ("/{id}")
    public String deleteTeacher(@PathVariable("id") int id){
        String message = new String("delete " + "teacher " + id);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(TEACHERS_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }
}