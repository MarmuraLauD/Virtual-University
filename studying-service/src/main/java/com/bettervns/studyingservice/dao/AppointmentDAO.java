package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.Appointment;
import com.bettervns.studyingservice.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Component
public class AppointmentDAO {

    private final AppointmentRepository AppointmentRepository;

    @Autowired
    public AppointmentDAO(AppointmentRepository AppointmentRepository) {
        this.AppointmentRepository = AppointmentRepository;
    }

    public List<Appointment> getAllAppointments() {
        return AppointmentRepository.findAll();
    }

    public Appointment getAppointmentById(int id) {
        Optional<Appointment> appointments = AppointmentRepository.findById(id);
        if (appointments.isPresent()) {
            return appointments.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public Appointment add(Appointment appointment) {
        return AppointmentRepository.save(appointment);
    }

    public void update(int id, Appointment updatedAppointment) {
        Optional<Appointment> optionalAppointment = AppointmentRepository.findById(id);
        if (optionalAppointment.isPresent()) {
            Appointment appointmenti = optionalAppointment.get();
            appointmenti.setBeginTime(updatedAppointment.getBeginTime());
            appointmenti.setEndTime(updatedAppointment.getEndTime());
            appointmenti.setDate(updatedAppointment.getDate());
            appointmenti.setMeetingLink(updatedAppointment.getMeetingLink());
            appointmenti.setTeacher(updatedAppointment.getTeacher());
            appointmenti.setCourse(updatedAppointment.getCourse());
            appointmenti.setAllowed_groups(updatedAppointment.getAllowed_groups());
            AppointmentRepository.save(appointmenti);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        AppointmentRepository.deleteById(id);
    }
}