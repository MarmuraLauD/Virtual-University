package com.bettervns.adminservice.controllers;

import com.bettervns.adminservice.requests.StudentRequest;
import com.bettervns.models.ERole;
import com.bettervns.models.User;
import com.bettervns.security.jwt.JwtUtils;
import com.google.gson.GsonBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/student")
public class StudentsController {
    private static final String STUDENTS_QUEUE_KEY = "studentsQueue";
    private static final String SECURITY_QUEUE_KEY = "securityQueue";
    private static final String DIRECT_EXCHANGE_NAME = "betterVNS-direct-exchange";

    private final RabbitTemplate template;

    @Autowired
    public StudentsController(RabbitTemplate rabbitTemplate) {
        this.template = rabbitTemplate;
    }

    @PostMapping()
    public ResponseEntity<?> createStudent(@RequestBody StudentRequest requestObject){
        User user = new User(requestObject.email(), JwtUtils.encodePassword(requestObject.password()), ERole.ROLE_STUDENT);
        String message = "create " + 0  + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(user);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(SECURITY_QUEUE_KEY, message);

        message = "create " + 0 + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDENTS_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully created");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateStudent(@RequestBody StudentRequest requestObject, @PathVariable("id") int id){
        String message = "update " + id + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson
                (new User(requestObject.email(), JwtUtils.encodePassword(requestObject.password()), ERole.ROLE_STUDENT));
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(SECURITY_QUEUE_KEY, message);

        message = "update " + id + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDENTS_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully updated");
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteStudent(@RequestParam int id, @RequestParam String email){
        String message = "delete " + id + " " + email;
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(SECURITY_QUEUE_KEY, message);
        message = "delete " + id;

        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDENTS_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully deleted");
    }
}