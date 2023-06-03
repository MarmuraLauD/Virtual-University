package com.bettervns.studyingservice.rabbitmq;

import com.bettervns.studyingservice.dao.AppointmentDAO;
import com.bettervns.studyingservice.dao.CourseDAO;
import com.bettervns.studyingservice.dao.DepartmentDAO;
import com.bettervns.studyingservice.dao.GroupDAO;
import com.bettervns.studyingservice.models.Appointment;
import com.bettervns.studyingservice.models.Course;
import com.bettervns.studyingservice.models.Department;
import com.bettervns.studyingservice.models.Group;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityMessageProcessor {
    private final DepartmentDAO departmentDAO;
    private final CourseDAO courseDAO;
    private final GroupDAO groupDAO;
    private final AppointmentDAO appointmentDAO;
    private final EntityMessageParser entityMessageParser;

    @Autowired
    public EntityMessageProcessor(GroupDAO groupDAO, DepartmentDAO departmentDAO, EntityMessageParser entityMessageParser,
                                  CourseDAO courseDAO, AppointmentDAO appointmentDAO) {
        this.departmentDAO = departmentDAO;
        this.groupDAO = groupDAO;
        this.entityMessageParser = entityMessageParser;
        this.courseDAO = courseDAO;
        this.appointmentDAO = appointmentDAO;
    }

    public void processMessage(String message) {
        System.out.println("Processor got: " + message);
        switch (entityMessageParser.getModelType(message)) {
            case "department" -> performDepartmentAction(message);
            case "course" -> performCourseAction(message);
            case "group" -> performGroupAction(message);
            case "appointment" -> performAppointmentAction(message);
        }
    }

    public void performDepartmentAction(String message) {
        switch (entityMessageParser.getOperationType(message)) {
            case "create" -> {
                departmentDAO.add(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(entityMessageParser.getMessageBody(message), Department.class));
            }
            case "update" -> {
                departmentDAO.update(entityMessageParser.getId(message), new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(entityMessageParser.getMessageBody(message), Department.class));
            }
            case "delete" -> departmentDAO.delete(entityMessageParser.getId(message));
        }
    }

    public void performGroupAction(String message) {
        switch (entityMessageParser.getOperationType(message)) {
            case "create" -> {
                groupDAO.add(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(entityMessageParser.getMessageBody(message), Group.class));
            }
            case "update" -> {
                groupDAO.update(entityMessageParser.getId(message), new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(entityMessageParser.getMessageBody(message), Group.class));
            }
            case "delete" -> groupDAO.delete(entityMessageParser.getId(message));
        }
    }

    public void performCourseAction(String message) {
        switch (entityMessageParser.getOperationType(message)) {
            case "create" -> {
                courseDAO.add(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(entityMessageParser.getMessageBody(message), Course.class));
            }
            case "update" -> {
                courseDAO.update(entityMessageParser.getId(message), new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(entityMessageParser.getMessageBody(message), Course.class));
            }
            case "delete" -> courseDAO.delete(entityMessageParser.getId(message));
        }
    }

    public void performAppointmentAction(String message) {
        switch (entityMessageParser.getOperationType(message)) {
            case "create" -> {
                appointmentDAO.add(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(entityMessageParser.getMessageBody(message), Appointment.class));
            }
            case "update" -> {
                appointmentDAO.update(entityMessageParser.getId(message), new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(entityMessageParser.getMessageBody(message), Appointment.class));
            }
            case "delete" -> appointmentDAO.delete(entityMessageParser.getId(message));
        }
    }
}