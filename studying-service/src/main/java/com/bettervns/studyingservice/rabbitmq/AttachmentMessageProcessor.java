package com.bettervns.studyingservice.rabbitmq;

import com.bettervns.studyingservice.dao.*;
import com.bettervns.studyingservice.models.AppointmentToGroup;
import com.bettervns.studyingservice.models.CourseToGroup;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMessageProcessor {
    private final AppointmentToGroupDAO appointmentToGroupDAO;
    private final CourseToGroupDAO courseToGroupDAO;
    private final AttachmentMessageParser attachmentMessageParser;

    @Autowired
    public AttachmentMessageProcessor(AppointmentToGroupDAO appointmentToGroupDAO, CourseToGroupDAO courseToGroupDAO, AttachmentMessageParser attachmentMessageParser) {
        this.appointmentToGroupDAO = appointmentToGroupDAO;
        this.courseToGroupDAO = courseToGroupDAO;
        this.attachmentMessageParser = attachmentMessageParser;
    }

    public void processMessage(String message) {
        System.out.println("Processor got: " + message);
        switch (attachmentMessageParser.getModelType(message)) {
            case "course_group" -> performCourseAction(message);
            case "appointment_group" -> performAppointmentAction(message);
        }
    }

    public void performCourseAction(String message) {
        courseToGroupDAO.add(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                fromJson(attachmentMessageParser.getMessageBody(message), CourseToGroup.class));
    }

    public void performAppointmentAction(String message) {
        switch (attachmentMessageParser.getOperationType(message)) {
            case "attach" -> appointmentToGroupDAO.add(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                    fromJson(attachmentMessageParser.getMessageBody(message), AppointmentToGroup.class));
            case "remove" -> appointmentToGroupDAO.removeAppointmentToGroup(
                    new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson
                            (attachmentMessageParser.getMessageBody(message), AppointmentToGroup.class).getGroupId(),
                    new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson
                            (attachmentMessageParser.getMessageBody(message), AppointmentToGroup.class).getAppointmentId()
            );
        }
    }
}