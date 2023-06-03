package com.bettervns.adminservice.controllers;

import com.bettervns.adminservice.requests.DepartmentRequest;
import com.google.gson.GsonBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/department")
public class DepartmentsController {

    private static final String DIRECT_EXCHANGE_NAME = "betterVNS-direct-exchange";
    private static final String STUDYING_ENTITY_QUEUE_KEY = "studyingEntityQueue";
    private final RabbitTemplate template;

    @Autowired
    public DepartmentsController(RabbitTemplate rabbitTemplate) {
        this.template = rabbitTemplate;
    }

    @PostMapping()
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentRequest requestObject){
        String message = "create " + "department " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_ENTITY_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully added");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateDepartment(@RequestBody DepartmentRequest requestObject, @PathVariable("id") int id){
        String message = "update " + "department " + id + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_ENTITY_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully added");

    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable("id") int id){
        String message = "delete " + "department " + id;
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_ENTITY_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully added");
    }
}