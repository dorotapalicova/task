package com.task.user_manager.exception;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
        final ResponseStatus status = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "User Not Found");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, status != null ? status.code() : HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handlePolicyAlreadyExists(UserAlreadyExistsException ex) {
        final ResponseStatus status = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Userename is not available");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, status != null ? status.code() : HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PolicyNotFoundException.class)
    public ResponseEntity<Object> handlePolicyNotFound(PolicyNotFoundException ex) {
        final ResponseStatus status = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Policy Not Found");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, status != null ? status.code() : HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PolicyAlreadyExistsException.class)
    public ResponseEntity<Object> handlePolicyAlreadyExists(PolicyAlreadyExistsException ex) {
        final ResponseStatus status = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Policy name is not available");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, status != null ? status.code() : HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalPolicyTypeException.class)
    public ResponseEntity<Object> handleIllegalPolicyType(IllegalPolicyTypeException ex) {
        final ResponseStatus status = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Policy type is not supported");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, status != null ? status.code() : HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralError(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}