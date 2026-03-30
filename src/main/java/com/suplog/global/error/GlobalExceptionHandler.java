package com.suplog.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        log.error("잘못된 요청 {}", e.getMessage());
        ErrorResponse response = ErrorResponse.of("400", "잘못된 요청입니다.");

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    @ExceptionHandler(SupLogException.class)
    public ResponseEntity<ErrorResponse> supLogException(SupLogException e) {
        log.error("비즈니스 오류: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.of(
                String.valueOf(e.getStatus().value()),
                e.getMessage()
        );

        return new ResponseEntity<>(response, e.getStatus());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse illegalArgumentException(IllegalArgumentException e) {
        log.error("잘못된 인자: {}", e.getMessage());
        return ErrorResponse.of("400", e.getMessage());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse generalServerError(Exception e) {
        log.error("서버 오류: {}", e.getMessage(), e);
        return ErrorResponse.of("500", "서버에 오류가 발생했습니다.");
    }
}
