package com.bettervns.adminservice.controllers;

import com.bettervns.adminservice.requests.TeacherRequest;
import com.google.gson.GsonBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    //TODO !! : create new user in jwt_secure when creating teacher or student. implement rabbit on security for this
    @PostMapping()
    public ResponseEntity<?> createTeacher(@RequestBody TeacherRequest requestObject){
        String message = "create " + 0 + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(TEACHERS_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully created");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTeacher(@RequestBody TeacherRequest requestObject, @PathVariable("id") int id){
        String message = "update " + id + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(TEACHERS_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully updated");

    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable("id") int id){
        String message = new String("delete " + id);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(TEACHERS_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully deleted");
    }
}