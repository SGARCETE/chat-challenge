package com.asapp.backend.challenge.config;

import com.asapp.backend.challenge.exceptions.ApiError;
import com.asapp.backend.challenge.exceptions.PasswordNotValidException;
import com.asapp.backend.challenge.exceptions.UserAlreadyExistsException;
import com.asapp.backend.challenge.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  Handle all controllers errors and returns them with a specific message for the users.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<ApiError> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        LOGGER.warn(String.format("Exception %s was thrown with message: content type must be STRING, VIDEO or IMAGe", ex.getClass(), ex.getMessage()));
        String message = "Content type must be STRING, VIDEO or IMAGE";
        ApiError apiError = new ApiError("Message not readable exception", message, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(value = {PasswordNotValidException.class})
    public ResponseEntity<ApiError> passwordNotValidException(PasswordNotValidException ex) {
        LOGGER.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("Password not valid exception", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<ApiError> userAlreadyExistsException(UserAlreadyExistsException ex) {
        LOGGER.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("User already exists Exception", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ApiError> userNotFoundException(UserNotFoundException ex) {
        LOGGER.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("User not found Exception", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ApiError> constraintViolationException(ConstraintViolationException ex) {
        LOGGER.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("Constraint Violation Exception", ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LOGGER.warn(String.format("Exception %s was thrown", ex.getClass()));

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "Method Argument Not Valid");

        //Get all errors
        List<String> errors = ex.getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        LOGGER.warn(String.format("Errors: %s", errors));

        body.put("errors", errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ApiError> handleUnknownException(HttpMessageNotReadableException ex) {
        LOGGER.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("Internal Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

}
