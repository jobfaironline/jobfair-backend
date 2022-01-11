package org.capstone.job_fair.controllers.exception;

public class AppException extends RuntimeException {

    private static final long serialVersionUID = 7258310602933545051L;

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
