package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.AppointmentToGroup;
import com.bettervns.studyingservice.repository.AppointmentToGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class AppointmentToGroupDAO {

    private final AppointmentToGroupRepository AppointmentToGroupRepository;

    @Autowired
    public AppointmentToGroupDAO(AppointmentToGroupRepository appointmentToGroupRepository) {
        this.AppointmentToGroupRepository = appointmentToGroupRepository;
    }

    public List<AppointmentToGroup> getAllAppointmentToGroups() {
        return AppointmentToGroupRepository.findAll();
    }

    public AppointmentToGroup getAppointmentToGroupById(int id) {
        Optional<AppointmentToGroup> appointmentToGroups = AppointmentToGroupRepository.findById(id);
        if (appointmentToGroups.isPresent()) {
            return appointmentToGroups.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public AppointmentToGroup add(AppointmentToGroup appointmentToGroup) {
        return AppointmentToGroupRepository.save(appointmentToGroup);
    }

    public void update(int id, AppointmentToGroup updatedAppointmentToGroup) {
        Optional<AppointmentToGroup> optionalAppointmentToGroup = AppointmentToGroupRepository.findById(id);
        if (optionalAppointmentToGroup.isPresent()) {
            AppointmentToGroup appointmentToGroupi = optionalAppointmentToGroup.get();
            appointmentToGroupi.setAppointmentId(updatedAppointmentToGroup.getAppointmentId());
            appointmentToGroupi.setGroupId(updatedAppointmentToGroup.getGroupId());
            AppointmentToGroupRepository.save(appointmentToGroupi);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        AppointmentToGroupRepository.deleteById(id);
    }
}