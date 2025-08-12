package com.task.user_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String name) {
        super("User name is not available. Name: " + name);
    }
}
