package com.task.user_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PolicyAlreadyExistsException extends RuntimeException {

    public PolicyAlreadyExistsException(String name) {
        super("Policy name is not available. Name: " + name);
    }
}
