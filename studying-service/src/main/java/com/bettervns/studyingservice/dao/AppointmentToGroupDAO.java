package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.Appointment;
import com.bettervns.studyingservice.models.AppointmentToGroup;
import com.bettervns.studyingservice.repository.AppointmentToGroupRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class AppointmentToGroupDAO {

    private final AppointmentToGroupRepository appointmentToGroupRepository;
    private final EntityManager entityManager;

    @Autowired
    public AppointmentToGroupDAO(AppointmentToGroupRepository appointmentToGroupRepository, EntityManager entityManager) {
        this.appointmentToGroupRepository = appointmentToGroupRepository;
        this.entityManager = entityManager;
    }

    public List<AppointmentToGroup> getAllAppointmentToGroups() {
        return appointmentToGroupRepository.findAll();
    }

    public AppointmentToGroup getAppointmentToGroupById(int id) {
        Optional<AppointmentToGroup> appointmentToGroups = appointmentToGroupRepository.findById(id);
        if (appointmentToGroups.isPresent()) {
            return appointmentToGroups.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public List<AppointmentToGroup> getAppointmentsToGroupsByGroupId(int groupId){
        TypedQuery<AppointmentToGroup> query = entityManager.createQuery(
                "SELECT a FROM AppointmentToGroup a WHERE a.groupId = :groupId", AppointmentToGroup.class);
        query.setParameter("groupId", groupId);
        List<AppointmentToGroup> resultList = query.getResultList();
        return resultList;
    }

    public List<AppointmentToGroup> getAppointmentsToGroupsByAppointmentId(int appointmentId){
        TypedQuery<AppointmentToGroup> query = entityManager.createQuery(
                "SELECT a FROM AppointmentToGroup a WHERE a.appointmentId = :appointmentId", AppointmentToGroup.class);
        query.setParameter("appointmentId", appointmentId);
        List<AppointmentToGroup> resultList = query.getResultList();
        return resultList;
    }

    public void removeAppointmentToGroup(int groupId, int appointmentId){
        TypedQuery<AppointmentToGroup> query = entityManager.createQuery(
                "SELECT a FROM AppointmentToGroup a WHERE a.appointmentId = :appointmentId AND a.groupId = :groupId", AppointmentToGroup.class);
        query.setParameter("groupId", groupId);
        query.setParameter("appointmentId", appointmentId);
        List<AppointmentToGroup> resultList = query.getResultList();
        appointmentToGroupRepository.delete(resultList.get(0));
    }

    public AppointmentToGroup add(AppointmentToGroup appointmentToGroup) {
        return appointmentToGroupRepository.save(appointmentToGroup);
    }

    public void update(int id, AppointmentToGroup updatedAppointmentToGroup) {
        Optional<AppointmentToGroup> optionalAppointmentToGroup = appointmentToGroupRepository.findById(id);
        if (optionalAppointmentToGroup.isPresent()) {
            AppointmentToGroup appointmentToGroupi = optionalAppointmentToGroup.get();
            appointmentToGroupi.setAppointmentId(updatedAppointmentToGroup.getAppointmentId());
            appointmentToGroupi.setGroupId(updatedAppointmentToGroup.getGroupId());
            appointmentToGroupRepository.save(appointmentToGroupi);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        appointmentToGroupRepository.deleteById(id);
    }
}