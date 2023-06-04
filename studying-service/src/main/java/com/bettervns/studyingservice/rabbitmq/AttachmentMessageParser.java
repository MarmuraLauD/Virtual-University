package com.bettervns.studyingservice.rabbitmq;

import org.springframework.stereotype.Component;

@Component
public class AttachmentMessageParser {

    public String getModelType(String message) {
        StringBuilder modelType = new StringBuilder();
        for (int i = message.indexOf(' ') + 1; i < message.length(); i++) {
            if (message.charAt(i) == ' ') {
                break;
            } else modelType.append(message.charAt(i));
        }
        return modelType.toString();
    }

    public String getOperationType(String message) {
        StringBuilder operationType = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == ' ') {
                break;
            } else {
                operationType.append(message.charAt(i));
            }
        }
        return operationType.toString();
    }

    public String getMessageBody(String message) {
        StringBuilder messageBody = new StringBuilder();
        for (int i = getSpaceIndexByNumber(message, 2);
             i < message.length(); i++) {
            messageBody.append(message.charAt(i));
        }
        return messageBody.toString();
    }

    private int getSpaceIndexByNumber(String string, int number) {
        int counter = 0;
        int index = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ') {
                counter++;
            }
            if (counter >= number) {
                index = i;
                break;
            }
        }
        return index;
    }
}