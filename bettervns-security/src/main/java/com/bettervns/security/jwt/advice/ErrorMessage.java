package com.bettervns.security.jwt.advice;


import java.util.Collections;
import java.util.List;

public class ErrorMessage {
    private final int status;
    private final String message;
    private final List<String> errors;

    public ErrorMessage(int status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ErrorMessage(int status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }
}
