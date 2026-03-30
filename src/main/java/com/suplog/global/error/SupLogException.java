package com.suplog.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class SupLogException extends RuntimeException {

    private final HttpStatus status;

    protected SupLogException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
