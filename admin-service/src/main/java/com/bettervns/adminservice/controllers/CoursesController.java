package com.bettervns.adminservice.controllers;

import com.bettervns.adminservice.requests.CourseRequest;
import com.bettervns.adminservice.requests.CourseToGroupRequest;
import com.google.gson.GsonBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/course")
public class CoursesController {

    private static final String DIRECT_EXCHANGE_NAME = "betterVNS-direct-exchange";
    private static final String STUDYING_ENTITY_QUEUE_KEY = "studyingEntityQueue";
    private final RabbitTemplate template;

    @Autowired
    public CoursesController(RabbitTemplate rabbitTemplate) {
        this.template = rabbitTemplate;
    }

    @PostMapping()
    public String createCourse(@RequestBody CourseRequest requestObject){
        String message = "create " + "course " + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_ENTITY_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @PatchMapping("/{id}")
    public String updateCourse(@RequestBody CourseRequest requestObject, @PathVariable("id") int id){
        String message = "update " + "course " + id + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_ENTITY_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @DeleteMapping ("/{id}")
    public String deleteCourse(@PathVariable("id") int id){
        String message = new String("delete " + "course " + id);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_ENTITY_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @PostMapping("/attach")
    public ResponseEntity<?> attachGroupToCourse(@RequestBody CourseToGroupRequest courseToGroupRequest){
        String message = new String("attach " + "course_group " +
                new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(courseToGroupRequest));
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_ENTITY_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully attached");
    }
}