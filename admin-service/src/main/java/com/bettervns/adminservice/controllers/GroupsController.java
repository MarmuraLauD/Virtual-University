package com.bettervns.adminservice.controllers;

import com.bettervns.adminservice.requests.NewGroupRequest;
import com.bettervns.adminservice.requests.NewStudentRequest;
import com.google.gson.GsonBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/group")
public class GroupsController {

    private static final String DIRECT_EXCHANGE_NAME = "betterVNS-direct-exchange";
    private static final String STUDYING_QUEUE_KEY = "studyingQueue";
    private final RabbitTemplate template;

    @Autowired
    public GroupsController(RabbitTemplate rabbitTemplate) {
        this.template = rabbitTemplate;
    }

    @PostMapping()
    public String createGroup(@RequestBody NewGroupRequest requestObject){
        System.out.println(requestObject);
        String message = "create " + "group " + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @PatchMapping("/{id}")
    public String updateGroup(@RequestBody NewGroupRequest requestObject, @PathVariable("id") int id){
        String message = "update " + "group " + id + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @DeleteMapping ("/{id}")
    public String deleteGroup(@PathVariable("id") int id){
        String message = new String("delete " + "group " + id);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }
}