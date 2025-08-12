package com.task.user_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PolicyNotFoundException extends RuntimeException {

    public PolicyNotFoundException(String name) {
        super("Policy not found with name: " + name);
    }
}