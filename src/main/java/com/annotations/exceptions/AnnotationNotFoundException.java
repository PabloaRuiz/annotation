package com.annotations.exceptions;

public class AnnotationNotFoundException extends ExceptionManager {

    public AnnotationNotFoundException(String message) {
        super(message);
    }

    public AnnotationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
