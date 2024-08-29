package com.annotations.exceptions;

public class CredentialsError extends ExceptionManager {

    public CredentialsError(String message) {
        super(message);
    }

    public CredentialsError(String message, Throwable cause) {
        super(message, cause);
    }
}
