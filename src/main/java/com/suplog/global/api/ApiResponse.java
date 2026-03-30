package com.suplog.global.api;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        int code,
        HttpStatus status,
        String message,
        T data
) {

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(httpStatus.value(), httpStatus, message, data);
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
        return of(httpStatus, httpStatus.name(), data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, data);
    }

    public static ApiResponse<Void> ok() {
        return of(HttpStatus.OK, null);
    }
}
