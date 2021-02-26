package com.backend.service.sign.in.web.exception;

public interface GlobalExceptionHandlerConfigurer {
    default void putApiErrorMappers(GlobalExceptionHandler.ApiErrorMappers mappers, ApiErrorMessageFactory apiError) {
    }
}
