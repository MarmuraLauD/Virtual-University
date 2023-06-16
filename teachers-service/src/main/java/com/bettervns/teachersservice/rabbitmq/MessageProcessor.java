package com.bettervns.teachersservice.rabbitmq;

import com.bettervns.teachersservice.dao.TeacherDAO;
import com.bettervns.teachersservice.models.Teacher;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class MessageProcessor {

    private final TeacherDAO TeacherDAO;
    private final MessageParser messageParser;

    @Autowired
    public MessageProcessor(TeacherDAO TeacherDAO, MessageParser messageParser) {
        this.TeacherDAO = TeacherDAO;
        this.messageParser = messageParser;
    }

    public void processMessage(String message){
        String operationType = messageParser.getOperationType(message);
        try {
            switch (operationType) {
                case "create" -> createTeacher(messageParser.getMessageBody(message));
                case "update" -> updateTeacher(messageParser.getMessageBody(message), messageParser.getId(message));
                case "delete" -> deleteTeacher(messageParser.getId(message));
            }
        } catch (ResponseStatusException e){
            if (e.getReason().equals("Unable to find resource")){
                System.out.println("No teacher with this id");
            }
            e.printStackTrace();
        }
    }

    public void deleteTeacher(int id){

        TeacherDAO.delete(id);
    }

    public void updateTeacher(String TeacherParams, int id) throws ResponseStatusException{
        Teacher Teacher = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(TeacherParams, Teacher.class);
        TeacherDAO.update(id, Teacher);
    }

    public void createTeacher(String TeacherParams) throws ResponseStatusException{
        Teacher Teacher = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(TeacherParams, Teacher.class);
        TeacherDAO.add(Teacher);
    }
}