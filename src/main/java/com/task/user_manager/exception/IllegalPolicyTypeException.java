package com.task.user_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IllegalPolicyTypeException  extends RuntimeException {

    public IllegalPolicyTypeException(Set<String> allowedKeys) {
        super("One or more condition keys are invalid. Allowed keys: " + allowedKeys);
    }

}
