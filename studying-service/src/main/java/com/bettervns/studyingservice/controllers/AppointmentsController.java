package com.bettervns.studyingservice.controllers;

import com.bettervns.studyingservice.dao.AppointmentDAO;
import com.bettervns.studyingservice.dao.AppointmentToGroupDAO;
import com.bettervns.studyingservice.models.Appointment;
import com.bettervns.studyingservice.models.AppointmentToGroup;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/studying")
public class AppointmentsController {

    private final AppointmentDAO appointmentDAO;
    private final AppointmentToGroupDAO appointmentToGroupDAO;

    @Autowired
    public AppointmentsController(AppointmentDAO appointmentDAO, AppointmentToGroupDAO appointmentToGroupDAO) {
        this.appointmentDAO = appointmentDAO;
        this.appointmentToGroupDAO = appointmentToGroupDAO;
    }

    @GetMapping("/appointments")
    public ResponseEntity<?> index(){
        return ResponseEntity.ok(new Gson().toJson(appointmentDAO.getAllAppointments()));
    }

    @GetMapping("/appointment/{id}")
    public ResponseEntity<?> show(@PathVariable("id") int id) {
        return ResponseEntity.ok(new Gson().toJson(appointmentDAO.getAppointmentById(id)));
    }

    // TODO : TRY java.util.Date here, and MB replace import to java.sql.Date
    @GetMapping("/appointments/week")
    public ResponseEntity<?> showAppointmentsForGroupByDate(@RequestParam int groupId){
        List<Integer> appointmentsIds = new ArrayList<>();
        List<Appointment> appointments = new ArrayList<>();
        List<AppointmentToGroup> appointmentToGroups = appointmentToGroupDAO.getAppointmentsToGroupsByGroupId(groupId);
        for (AppointmentToGroup i : appointmentToGroups){
            if (!appointmentsIds.contains(i.getAppointmentId())) appointmentsIds.add(i.getAppointmentId());
        }

        LocalDate currentDate = LocalDate.now();
        Date mondayDate = Date.valueOf(currentDate);
        Date sundayDate = Date.valueOf(currentDate);
        for (int i = 0; i < currentDate.getDayOfWeek().getValue() - 1; i++){
            mondayDate.setDate(mondayDate.getDate() - 1);
        }
        for (int i = 0; i < 7 - currentDate.getDayOfWeek().getValue(); i++){
            sundayDate.setDate(sundayDate.getDate() + 1);
        }
        for (Integer i : appointmentsIds){
           if (appointmentDAO.getAppointmentById(i).getDate().compareTo(mondayDate) >= 0 &&
                   appointmentDAO.getAppointmentById(i).getDate().compareTo(sundayDate) <= 0)
                   appointments.add(appointmentDAO.getAppointmentById(i));
        }
        return ResponseEntity.ok(new Gson().toJson(appointments));
    }
}