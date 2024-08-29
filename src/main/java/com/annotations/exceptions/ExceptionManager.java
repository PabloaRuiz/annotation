package com.annotations.exceptions;

public class ExceptionManager extends RuntimeException {

    public ExceptionManager(String message) {
        super(message);
    }

    public ExceptionManager(String message, Throwable cause) {
        super(message, cause);
    }
}
