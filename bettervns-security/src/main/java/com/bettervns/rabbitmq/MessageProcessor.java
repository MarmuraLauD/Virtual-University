package com.bettervns.rabbitmq;

import com.bettervns.dao.UserDAO;
import com.bettervns.models.ERole;
import com.bettervns.models.Role;
import com.bettervns.models.User;
import com.bettervns.models.UserModel;
import com.bettervns.repository.UserRepository;
import com.google.gson.GsonBuilder;

import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessor {

    private final UserDAO userDAO;

    private final MessageParser messageParser;

    private final UserRepository userRepository;

    public MessageProcessor(UserRepository userRepository, MessageParser messageParser, UserDAO userDAO) {
        this.userRepository = userRepository;
        this.messageParser = messageParser;
        this.userDAO = userDAO;
    }

    public void processMessage(String message) {
        System.out.println("Processor got: " + message);
        performAction(message);
    }

    public void performAction(String message){
        System.out.println(messageParser.getOperationType(message));
        System.out.println(messageParser.getModelType(message));
        System.out.println(messageParser.getMessageBody(message));
        switch (messageParser.getOperationType(message)) {
            case "create" -> {
                UserModel userModel = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(messageParser.getMessageBody(message), UserModel.class);
                User user = new User(userModel.getEmail(), userModel.getPassword());
                switch (userModel.getRole()){
                    case "ROLE_STUDENT" -> user.setRole(new Role(ERole.ROLE_STUDENT));
                    case "ROLE_TEACHER" -> user.setRole(new Role(ERole.ROLE_TEACHER));
                    case "ROLE_ADMIN" -> user.setRole(new Role(ERole.ROLE_ADMIN));
                }
                userDAO.add(user);
            }
            case "update" -> {
                UserModel userModel = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().
                        fromJson(messageParser.getMessageBody(message), UserModel.class);
                User user = new User(userModel.getEmail(), userModel.getPassword());
                switch (userModel.getRole()){
                    case "ROLE_STUDENT" -> user.setRole(new Role(ERole.ROLE_STUDENT));
                    case "ROLE_TEACHER" -> user.setRole(new Role(ERole.ROLE_TEACHER));
                    case "ROLE_ADMIN" -> user.setRole(new Role(ERole.ROLE_ADMIN));
                }
                userDAO.update(user);
            }
            case "delete" -> userDAO.deleteByEmail(messageParser.getMessageBody(message));
        }
    }
}