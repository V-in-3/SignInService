package com.backend.service.sign.in.web.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import static org.springframework.http.HttpStatus.*;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final Object[] args;
    private final String errorCode;

    public ApiException(@NonNull HttpStatus httpStatus, @NonNull String codeOrMessage, @Nullable Object... args) {
        super(codeOrMessage);
        this.httpStatus = httpStatus;
        this.args = args;
        this.errorCode = null;
    }

    public ApiException(@NonNull Throwable cause, @NonNull HttpStatus httpStatus, @NonNull String codeOrMessage, @Nullable Object... args) {
        super(codeOrMessage, cause);
        this.httpStatus = httpStatus;
        this.args = args;
        this.errorCode = null;
    }

    public static ApiException badRequest(@NonNull String codeOrMessage, @Nullable Object... args) {
        return new ApiException(BAD_REQUEST, codeOrMessage, args);
    }

    public static ApiException notFound(@NonNull String codeOrMessage, @Nullable Object... args) {
        return new ApiException(NOT_FOUND, codeOrMessage, args);
    }

    public static ApiException unprocessableEntity(@NonNull String codeOrMessage, @Nullable Object... args) {
        return new ApiException(UNPROCESSABLE_ENTITY, codeOrMessage, args);
    }

    public static ApiException internalServerError(@NonNull String codeOrMessage, @Nullable Object... args) {
        return new ApiException(INTERNAL_SERVER_ERROR, codeOrMessage, args);
    }
}