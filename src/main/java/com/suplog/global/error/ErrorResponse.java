package com.suplog.global.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code,
        String message,
        Map<String, String> validation
) {

    public ErrorResponse {
        validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        validation.put(fieldName, errorMessage);
    }

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, new HashMap<>());
    }
}
