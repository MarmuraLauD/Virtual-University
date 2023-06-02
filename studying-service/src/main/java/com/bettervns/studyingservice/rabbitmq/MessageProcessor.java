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
public class MessageProcessor {
    private final DepartmentDAO departmentDAO;
    private final CourseDAO courseDAO;
    private final GroupDAO groupDAO;
    private final AppointmentDAO appointmentDAO;
    private final MessageParser messageParser;

    @Autowired
    public MessageProcessor(GroupDAO groupDAO, DepartmentDAO departmentDAO, MessageParser messageParser,
                            CourseDAO courseDAO, AppointmentDAO appointmentDAO) {
        this.departmentDAO = departmentDAO;
        this.groupDAO = groupDAO;
        this.messageParser = messageParser;
        this.courseDAO = courseDAO;
        this.appointmentDAO = appointmentDAO;
    }

    public void processMessage(String message) {
        System.out.println("Processor got: " + message);
        switch (messageParser.getModelType(message)) {
            case "department" -> performDepartmentAction(message);
            case "course" -> performCourseAction(message);
            case "group" -> performGroupAction(message);
            case "appointment" -> performAppointmentAction(message);
        }
    }

    public void performDepartmentAction(String message) {
        switch (messageParser.getOperationType(message)) {
            case "create" -> {
                departmentDAO.add(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(messageParser.getMessageBody(message), Department.class));
            }
            case "update" -> {
                departmentDAO.update(messageParser.getId(message), new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(messageParser.getMessageBody(message), Department.class));
            }
            case "delete" -> departmentDAO.delete(messageParser.getId(message));
        }
    }

    public void performGroupAction(String message) {
        switch (messageParser.getOperationType(message)) {
            case "create" -> {
                groupDAO.add(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(messageParser.getMessageBody(message), Group.class));
            }
            case "update" -> {
                groupDAO.update(messageParser.getId(message), new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(messageParser.getMessageBody(message), Group.class));
            }
            case "delete" -> groupDAO.delete(messageParser.getId(message));
        }
    }

    public void performCourseAction(String message) {
        switch (messageParser.getOperationType(message)) {
            case "create" -> {
                courseDAO.add(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(messageParser.getMessageBody(message), Course.class));
            }
            case "update" -> {
                courseDAO.update(messageParser.getId(message), new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(messageParser.getMessageBody(message), Course.class));
            }
            case "delete" -> courseDAO.delete(messageParser.getId(message));
        }
    }

    public void performAppointmentAction(String message) {
        switch (messageParser.getOperationType(message)) {
            case "create" -> {
                appointmentDAO.add(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(messageParser.getMessageBody(message), Appointment.class));
            }
            case "update" -> {
                appointmentDAO.update(messageParser.getId(message), new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(messageParser.getMessageBody(message), Appointment.class));
            }
            case "delete" -> appointmentDAO.delete(messageParser.getId(message));
        }
    }
}