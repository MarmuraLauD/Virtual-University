package com.bettervns.studyingservice.rabbitmq;

import com.bettervns.studyingservice.dao.DepartmentDAO;
import com.bettervns.studyingservice.dao.GroupDAO;
import com.bettervns.studyingservice.models.Department;
import com.bettervns.studyingservice.models.Group;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessor {
    // TODO : implement all models DAOs and inject here.
    private final DepartmentDAO departmentDAO;
    private final GroupDAO groupDAO;
    private final MessageParser messageParser;

    @Autowired
    public MessageProcessor(GroupDAO groupDAO, DepartmentDAO departmentDAO, MessageParser messageParser) {
        this.departmentDAO = departmentDAO;
        this.groupDAO = groupDAO;
        this.messageParser = messageParser;
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
                departmentDAO.addDepartment(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
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
                groupDAO.addGroup(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(messageParser.getMessageBody(message), Group.class));
            }
            case "update" -> {
                groupDAO.update(messageParser.getId(message), new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(messageParser.getMessageBody(message), Group.class));
            }
            case "delete" -> groupDAO.delete(messageParser.getId(message));
        }
    }

    // TODO : Implement DAOs and implement that methods.
    public void performCourseAction(String message) {

    }

    public void performAppointmentAction(String message) {

    }
}