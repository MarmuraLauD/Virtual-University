package com.bettervns.adminservice.controllers;

import com.bettervns.adminservice.requests.NewDepartmentRequest;
import com.bettervns.adminservice.requests.NewGroupRequest;
import com.google.gson.GsonBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/department")
public class DepartmentsController {

    private static final String DIRECT_EXCHANGE_NAME = "betterVNS-direct-exchange";
    private static final String STUDYING_QUEUE_KEY = "studyingQueue";
    private final RabbitTemplate template;

    @Autowired
    public DepartmentsController(RabbitTemplate rabbitTemplate) {
        this.template = rabbitTemplate;
    }

    @PostMapping()
    public String createDepartment(@RequestBody NewDepartmentRequest requestObject){
        System.out.println(requestObject);
        String message = "create " + "department " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @PatchMapping("/{id}")
    public String updateDepartment(@RequestBody NewDepartmentRequest requestObject, @PathVariable("id") int id){
        String message = "update " + "department " + id + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @DeleteMapping ("/{id}")
    public String deleteDepartment(@PathVariable("id") int id){
        String message = new String("delete " + "department " + id);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }
}
