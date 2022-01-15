package org.capstone.job_fair.controllers.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.models.custom_exceptions.AppException;
import org.capstone.job_fair.response.ErrorResponse;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.*;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> customException(JwtException ex) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_BAD_REQUEST", ""), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> noSuchElementFoundException(NoSuchElementException ex) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> unauthorizedException(AccessDeniedException ex) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_NO_PERMISSION", ""), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> customException(AppException ex) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_INTERNAL_ERROR", ""), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> MethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_METHOD_ARGUMENT_NOT_VALID", ""));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_HTTP_MESSAGE_NOT_VALID", ""), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_ENTITY_NOT_FOUND"), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus) {
        return ErrorResponse.build(httpStatus, message, exception.getCause().getMessage());
    }

    private ResponseEntity<?> buildErrorResponse(MethodArgumentNotValidException exception, String message) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ErrorResponse.build(HttpStatus.BAD_REQUEST, message, errors);
    }
}
