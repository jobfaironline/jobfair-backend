package org.capstone.job_fair.controllers.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.payload.GenericMessageResponseEntity;
import org.capstone.job_fair.response.ErrorResponse;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.context.ApplicationContextException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.*;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handlerGlobalException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Oops!";
        log.error(e.getMessage());
        return new ResponseEntity(message, status);
    }

    public ResponseEntity handleEntityNotFound(){

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> customException(JwtException ex, WebRequest request) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_BAD_REQUEST", ""), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> noSuchElementFoundException(NoSuchElementException ex, WebRequest request) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> unauthorizedException(AccessDeniedException ex, WebRequest request) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_NO_PERMISSION", ""), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> customException(AppException ex, WebRequest request) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_INTERNAL_ERROR", ""), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_METHOD_ARGUMENT_NOT_VALID", ""), HttpStatus.BAD_REQUEST, request);
    }

    public GenericMessageResponseEntity handleEntityNotFound() {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Entity not found!";
        return new ResponseEntity(message, status);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus,
                                                             WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message + exception, new Date());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(MethodArgumentNotValidException exception, String message, HttpStatus httpStatus,
                                                             WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String messageError = "";
        for (FieldError fieldError : fieldErrors
        ) {
            messageError = messageError.concat(fieldError.getField() + " \\ " + fieldError.getDefaultMessage());
        }
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), messageError, new Date());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
