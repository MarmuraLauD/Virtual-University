package com.bettervns.adminservice.controllers;

import com.bettervns.adminservice.requests.AppointmentRequest;
import com.bettervns.adminservice.requests.AppointmentToGroupRequest;
import com.google.gson.GsonBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/appointment")
public class AppointmentsController {

    private static final String DIRECT_EXCHANGE_NAME = "betterVNS-direct-exchange";
    private static final String STUDYING_ENTITY_QUEUE_KEY = "studyingEntityQueue";
    private final RabbitTemplate template;

    @Autowired
    public AppointmentsController(RabbitTemplate rabbitTemplate) {
        this.template = rabbitTemplate;
    }

    @PostMapping()
    public String createAppointment(@RequestBody AppointmentRequest requestObject){
        String message = "create " + "appointment " + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_ENTITY_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @PatchMapping("/{id}")
    public String updateAppointment(@RequestBody AppointmentRequest requestObject, @PathVariable("id") int id){
        String message = "update " + "appointment " + id + " " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(requestObject);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_ENTITY_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @DeleteMapping ("/{id}")
    public String deleteAppointment(@PathVariable("id") int id){
        String message = new String("delete " + "appointment " + id);
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_ENTITY_QUEUE_KEY, message);
        return "redirect:/admin/1";
    }

    @PostMapping("/attach")
    public ResponseEntity<?> attachGroupToAppointment(@RequestBody AppointmentToGroupRequest appointmentToGroupRequest){
        String message = new String("attach " + "appointment_group " + new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(appointmentToGroupRequest));
        System.out.println(message);
        template.setExchange(DIRECT_EXCHANGE_NAME);
        template.convertAndSend(STUDYING_ENTITY_QUEUE_KEY, message);
        return ResponseEntity.ok("Successfully attached");
    }

}