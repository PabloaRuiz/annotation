package com.annotations.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessages {

    NOT_FOUND_ANNOTATION("Annotation not found"),
    INVALID_CREDENTIAL("Invalid credential"),
    ERROR_JSON("Error processing the JSON");

    private final String message;

}
