package com.bettervns.rabbitmq;

import com.bettervns.dao.UserDAO;
import com.bettervns.models.User;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class MessageProcessor {

    private final UserDAO userDAO;
    private final MessageParser messageParser;

    @Autowired
    public MessageProcessor(UserDAO userDAO, MessageParser messageParser) {
        this.userDAO = userDAO;
        this.messageParser = messageParser;
    }

    public void processMessage(String message){
        String operationType = messageParser.getOperationType(message);
        switch (operationType) {
            case "create" -> createUser(messageParser.getMessageBody(message));
            case "update" -> updateUser(messageParser.getMessageBody(message), messageParser.getId(message));
            case "delete" -> deleteUser(messageParser.getMessageBody(message));
        }
    }

    public void deleteUser(String email){
        try {
            User user = userDAO.getUserByEmail(email);
            System.out.println(Integer.parseInt(user.getId().toString()));
            userDAO.delete(Integer.parseInt(user.getId().toString()));
        }
        catch (ResponseStatusException e){
            System.out.println("No users with this email");
            e.printStackTrace();
        }
    }

    public void updateUser(String userParams, int id){
        User user = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(userParams, User.class);
        System.out.println();
        userDAO.update(id, user);
    }

    public void createUser(String userParams){
        System.out.println(userParams);
        User user = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(userParams, User.class);
        userDAO.add(user);
    }
}