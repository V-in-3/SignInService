package com.backend.service.sign.in.web.exception;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Clock;
import java.time.Instant;

import static org.springframework.http.HttpStatus.*;

@Slf4j
public class ApiErrorMessageFactory {

    Clock clock;

    public ApiErrorMessageFactory() {
        this.clock = Clock.systemUTC();
    }

    public ApiErrorMessage internalServerError(@NonNull String codeOrMessage, @Nullable Object... args) {
        return build(codeOrMessage, INTERNAL_SERVER_ERROR, args);
    }

    public ApiErrorMessage badRequest(@NonNull String codeOrMessage, @Nullable Object... args) {
        return build(codeOrMessage, BAD_REQUEST, args);
    }

    public ApiErrorMessage notFound(@NonNull String codeOrMessage, @Nullable Object... args) {
        return build(codeOrMessage, NOT_FOUND, args);
    }

    public ApiErrorMessage unprocessableEntity(@NonNull String codeOrMessage, @Nullable Object... args) {
        return build(codeOrMessage, UNPROCESSABLE_ENTITY, args);
    }

    public ApiErrorMessage methodNotAllowed(@NonNull String codeOrMessage, @Nullable Object... args) {
        return build(codeOrMessage, METHOD_NOT_ALLOWED, args);
    }

    public ApiErrorMessage.Error error(@NonNull String codeOrMessage) {
        return new ApiErrorMessage.Error(null, null, codeOrMessage, null);
    }

    public ApiErrorMessage.Error error(
            @NonNull String codeOrMessage,
            @Nullable String object,
            @Nullable String property,
            @Nullable Object invalidValue
    ) {
        return new ApiErrorMessage.Error(object, property, codeOrMessage, invalidValue);
    }

    public ApiErrorMessage.Error error(@NonNull ObjectError err) {
        String localizedMessage = err.getDefaultMessage();
        if (err instanceof FieldError) {
            return new ApiErrorMessage.Error(
                    err.getObjectName(),
                    ((FieldError) err).getField(),
                    localizedMessage,
                    ((FieldError) err).getRejectedValue()
            );
        } else {
            return new ApiErrorMessage.Error(err.getObjectName(), null, localizedMessage, null);
        }
    }

    ApiErrorMessage build(String codeOrMessage, HttpStatus httpStatus, @Nullable Object... args) {
        Instant timestamp = Instant.now(clock);
        var attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String path = attributes.getRequest().getRequestURI();

        return new ApiErrorMessage(timestamp, httpStatus, codeOrMessage, path);
    }
}
