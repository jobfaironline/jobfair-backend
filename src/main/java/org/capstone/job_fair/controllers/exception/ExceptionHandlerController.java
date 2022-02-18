package org.capstone.job_fair.controllers.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.responses.ErrorResponse;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(ex, MessageUtil.getMessage(MessageConstant.Exception.RESOURCE_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> customException(JwtException ex) {
        return buildErrorResponse(ex, MessageUtil.getMessage(MessageConstant.Exception.JWT_ERROR), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> noSuchElementFoundException(NoSuchElementException ex) {
        return buildErrorResponse(ex, MessageUtil.getMessage(MessageConstant.Exception.RESOURCE_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> unauthorizedException(AccessDeniedException ex) {
        return buildErrorResponse(ex, MessageUtil.getMessage(MessageConstant.Exception.NO_PERMISSION), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> MethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return buildErrorResponse(ex, MessageUtil.getMessage(MessageConstant.Exception.METHOD_ARGUMENT_NOT_VALID));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        return buildErrorResponse(ex, MessageUtil.getMessage(MessageConstant.Exception.HTTP_MESSAGE_NOT_VALID), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildErrorResponse(ex, MessageUtil.getMessage(MessageConstant.Exception.ENTITY_NOT_FOUND), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        ex.printStackTrace();
        return buildErrorResponse(ex, MessageUtil.getMessage(MessageConstant.Exception.INTERNAL_ERROR), HttpStatus.BAD_REQUEST);
    }



    private ResponseEntity<?> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus) {
        return ErrorResponse.build(httpStatus, message, exception.getClass().getSimpleName() + ": " + exception.getMessage());
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
