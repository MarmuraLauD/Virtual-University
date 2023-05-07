package com.bettervns.studentsservice.rabbitmq;

import org.springframework.stereotype.Component;

@Component
public class MessageParser {

    public String getOperationType(String message){
        StringBuilder operationType = new StringBuilder();
        for (int i = 0; i < message.length(); i++){
            if (message.charAt(i) == ' '){
                break;
            }
            else {
                operationType.append(message.charAt(i));
            }
        }
        return operationType.toString();
    }

    public int getId(String message){
        int spaceIndex = message.indexOf(' ');
        StringBuilder idString = new StringBuilder();
        for (int i = spaceIndex + 1; i < message.length(); i++){
            if (Character.isDigit(message.charAt(i))){
                idString.append(message.charAt(i));
            }
            else break;
        }
        return Integer.parseInt(idString.toString());
    }

    public String getMessageBody(String message){
        StringBuilder messageBody = new StringBuilder();
        int secondSpaceIndex = getSecondSpaceIndex(message);
        for (int i = secondSpaceIndex + 1; i < message.length(); i++) {
            messageBody.append(message.charAt(i));
        }
            return messageBody.toString();
    }

    private int getSecondSpaceIndex(String string) {
        int counter = 0;
        int index = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' '){
                counter++;
            }
            if (counter >= 2){
                index = i;
                break;
            }
        }
        return index;
    }
}