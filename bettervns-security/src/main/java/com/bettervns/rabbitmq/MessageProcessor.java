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
        try {
            switch (operationType) {
                case "create" -> createUser(messageParser.getMessageBody(message));
                case "update" -> updateUser(messageParser.getMessageBody(message), messageParser.getEmail(message));
                case "delete" -> deleteUser(messageParser.getMessageBody(message));
            }
        } catch (ResponseStatusException e){
            if (e.getReason().equals("Unable to find resource")) {
                System.out.println("No user with this email");
            }
            e.printStackTrace();
        }
    }

    public void deleteUser(String email) throws ResponseStatusException{
        User user = userDAO.getUserByEmail(email);
        System.out.println(Integer.parseInt(user.getId().toString()));
        userDAO.delete(Integer.parseInt(user.getId().toString()));
    }

    public void updateUser(String newUserParams, String oldEmail) throws ResponseStatusException{
        User oldUser = userDAO.getUserByEmail(oldEmail);
        User newUser = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(newUserParams, User.class);
        newUser.setId(oldUser.getId());
        userDAO.update(Integer.parseInt(oldUser.getId().toString()), newUser);
    }

    public void createUser(String userParams) throws ResponseStatusException{
        User user = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(userParams, User.class);
        userDAO.add(user);
    }
}