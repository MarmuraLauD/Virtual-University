package com.bettervns.studyingservice.rabbitmq;

import com.bettervns.studyingservice.dao.DepartmentDAO;
import com.bettervns.studyingservice.models.Department;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessor {
    // TODO : implement all models DAOs and inject here.
    private final DepartmentDAO departmentDAO;
    private final MessageParser messageParser;

    @Autowired
    public MessageProcessor(DepartmentDAO departmentDAO, MessageParser messageParser) {
        this.departmentDAO = departmentDAO;
        this.messageParser = messageParser;
    }

    public void processMessage(String message) {
        System.out.println("Processor got: " + message);
        System.out.println(messageParser.getOperationType(message));
        System.out.println(messageParser.getModelType(message));
        //System.out.println(messageParser.getId(message));
        System.out.println(messageParser.getMessageBody(message));
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

    // TODO : Implement DAOs and implement that methods.
    public void performGroupAction(String message) {

    }

    public void performCourseAction(String message) {

    }

    public void performAppointmentAction(String message) {

    }
}