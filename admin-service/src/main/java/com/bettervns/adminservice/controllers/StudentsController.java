package com.bettervns.adminservice.controllers;

import com.bettervns.adminservice.requests.StudentRequest;
import com.google.gson.GsonBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/student")
public class StudentsController {

    private static final String STUDENTS_QUEUE_KEY = "studentsQueue";
    private static final String DIRECT_EXCHANGE_NAME = "betterVNS-direct-exchange";

    private final RabbitTemplate template;

    @Autowired
    public StudentsController(RabbitTemplate rabbitTemplate) {
        this.template = rabbitTemplate;
    }

    @PostMapping()
    public ResponseEntity<?> createStudent(@RequestBody StudentRequest requestObject){
        String message = "create " + 0 + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDENTS_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully created");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateStudent(@RequestBody StudentRequest requestObject, @PathVariable("id") int id){
        String message = "update " + id + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDENTS_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully updated");
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") int id){
        String message = new String("delete " + id);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDENTS_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully deleted");
    }
}