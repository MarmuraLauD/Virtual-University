package com.bettervns.studyingservice.controllers;

import com.bettervns.studyingservice.dao.AppointmentDAO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/studying")
public class AppointmentsController {

    private final AppointmentDAO appointmentDAO;

    @Autowired
    public AppointmentsController(AppointmentDAO appointmentDAO) {
        this.appointmentDAO = appointmentDAO;
    }

    @GetMapping("/appointments")
    public ResponseEntity<?> index(){
        return ResponseEntity.ok(new Gson().toJson(appointmentDAO.index()));
    }

    @GetMapping("/appointment/{id}")
    public ResponseEntity<?> show(@PathVariable("id") int id) {
        return ResponseEntity.ok(new Gson().toJson(appointmentDAO.show(id)));
    }

    // TODO : TRY java.util.Date here, and MB replace import to java.sql.Date
    @GetMapping("/appointment")
    public ResponseEntity<?> showAppointmentsForGroupByDate(@RequestParam(name = "group") int group_id, @RequestParam Date date){
        return ResponseEntity.ok(new Gson().toJson(appointmentDAO.showForGroupByDate(group_id, date)));
    }
}