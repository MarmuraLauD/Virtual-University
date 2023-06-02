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
        List<AppointmentToGroup> appointmentToGroups = appointmentToGroupDAO.getAppointmentsToGroupsByGroupId(groupId);
        for (AppointmentToGroup i : appointmentToGroups){
            if (!appointmentsIds.contains(i.getAppointmentId())) appointmentsIds.add(i.getAppointmentId());
        }
        LocalDate nowDate = LocalDate.now();
        Date mondayDate = Date.valueOf(nowDate);
        Date sundayDate = Date.valueOf(nowDate);
        nowDate = null;
        for (int i = 0; i < nowDate.getDayOfWeek().getValue() - 1; i++){
            mondayDate.setDate(mondayDate.getDate() - 1);
        }
        List<List<Appointment>> weeklist = new ArrayList<>();
        Date currentDate = mondayDate;
        for (int i = 0; i < 7; i++) {
            weeklist.add(new ArrayList<Appointment>());
            currentDate.setDate(currentDate.getDate() + 1);
            for (Integer j : appointmentsIds) {
                if (appointmentDAO.getAppointmentById(j).getDate().compareTo(currentDate) == 0) {
                    weeklist.get(i).add(appointmentDAO.getAppointmentById(j));
                }
            }
        }

        return ResponseEntity.ok(new Gson().toJson(weeklist));
    }
}