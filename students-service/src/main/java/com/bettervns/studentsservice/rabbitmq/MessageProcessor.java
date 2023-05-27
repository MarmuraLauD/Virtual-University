package com.bettervns.studentsservice.rabbitmq;

import com.bettervns.studentsservice.dao.StudentDAO;
import com.bettervns.studentsservice.models.Student;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MessageProcessor {

    private final StudentDAO studentDAO;
    private final MessageParser messageParser;

    @Autowired
    public MessageProcessor(StudentDAO studentDAO, MessageParser messageParser) {
        this.studentDAO = studentDAO;
        this.messageParser = messageParser;
    }

    public void processMessage(String message){
        String operationType = messageParser.getOperationType(message);
        switch (operationType) {
            case "create" -> createStudent(messageParser.getMessageBody(message), messageParser.getId(message));
            case "update" -> updateStudent(messageParser.getMessageBody(message), messageParser.getId(message));
            case "delete" -> deleteStudent(messageParser.getId(message));
        }
    }

    public void deleteStudent(int id){
        studentDAO.delete(id);
    }

    public void updateStudent(String studentParams, int id){
        Student student = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(studentParams, Student.class);
        studentDAO.update(id, student);
    }

    public void createStudent(String studentParams, int id){
        Student student = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(studentParams, Student.class);
        System.out.println(id);
        student.setGroupId(id);
        studentDAO.add(student);
    }
}