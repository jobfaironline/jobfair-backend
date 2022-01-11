package org.capstone.job_fair.controllers.exception;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.payload.GenericMessageResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public GenericMessageResponseEntity handlerGlobalException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Oops!";
        log.error(e.getMessage());
        return new GenericMessageResponseEntity(message, status);
    }

    public GenericMessageResponseEntity handleEntityNotFound(){
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Entity not found!";
        return new GenericMessageResponseEntity(message, status);
    }
}