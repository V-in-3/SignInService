package com.backend.service.sign.in.web.exception;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
@ControllerAdvice
@Configuration
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String VALIDATION_FAILED = "validation.failed";

    private final ApiErrorMappers apiErrorMappers = new ApiErrorMappers();
    private final ApiErrorMessageFactory apiErrorMessageFactory;

    public GlobalExceptionHandler(ApiErrorMessageFactory apiErrorMessageFactory) {
        this.apiErrorMessageFactory = apiErrorMessageFactory;
    }

    @Autowired
    protected void initMappers(Map<String, GlobalExceptionHandlerConfigurer> configurerBeans) {
        if (!CollectionUtils.isEmpty(configurerBeans)) {
            Collection<GlobalExceptionHandlerConfigurer> configurers = new TreeMap<>(configurerBeans).values();
            for (GlobalExceptionHandlerConfigurer configurer : configurers) {
                configurer.putApiErrorMappers(apiErrorMappers, apiErrorMessageFactory);
            }
        }
    }

    @Configuration("_defaultGlobalExceptionHandlerConfig")
    protected static class DefaultConfig implements GlobalExceptionHandlerConfigurer {

        @Bean
        ApiErrorMessageFactory apiErrorMessageFactory() {
            return new ApiErrorMessageFactory();
        }

        /**
         * Puts default mappings to {@link ApiErrorMessage} for the following exceptions:
         * <ul>
         * 	<li>{@link ApiException}
         * 	<li>{@link MethodArgumentNotValidException}
         * 	<li>{@link ConstraintViolationException}
         *
         * @param mappers {@link ApiErrorMappers} must not be null
         */
        @Override
        public void putApiErrorMappers(ApiErrorMappers mappers, ApiErrorMessageFactory apiError) {

            mappers.put(ApiException.class, ex -> apiError.build(ex.getMessage(), ex.getHttpStatus(), ex.getArgs()));

            mappers.put(MethodArgumentNotValidException.class, ex -> {
                ApiErrorMessage errorMessage = apiError.unprocessableEntity(VALIDATION_FAILED);
                ex.getBindingResult().getAllErrors().forEach(error -> errorMessage.addError(apiError.error(error)));
                return errorMessage;
            });

            mappers.put(ConstraintViolationException.class, ex -> {
                ApiErrorMessage errorMessage = apiError.unprocessableEntity(VALIDATION_FAILED);
                ex.getConstraintViolations().forEach(error ->
                        errorMessage.addError(apiError.error(
                                error.getRootBeanClass().getSimpleName(),
                                ((PathImpl) error.getPropertyPath()).getLeafNode().asString(),
                                error.getMessage(),
                                error.getInvalidValue()
                        ))
                );
                return errorMessage;
            });
        }
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatus status,
            @NonNull WebRequest request
    ) {
        ApiErrorMessage apiErrorMessage = apiErrorMappers.lookupApiErrorMessage(ex).orElseGet(() -> apiErrorMessageFactory.build(NestedExceptionUtils.getMostSpecificCause(ex).getMessage(), status));
        return new ResponseEntity<>(apiErrorMessage, headers, apiErrorMessage.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(@NonNull Exception ex, @NonNull ServletWebRequest request) {
        ApiErrorMessage apiErrorMessage = apiErrorMappers.lookupApiErrorMessage(ex).orElseGet(() -> apiErrorMessageFactory.internalServerError(NestedExceptionUtils.getMostSpecificCause(ex).getMessage()));


        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getHttpStatus());
    }

    public static class ApiErrorMappers {
        private final Map<Class<? extends Exception>, Function<? extends Exception, ApiErrorMessage>> mappers = new ConcurrentHashMap<>();

        public <E extends Exception> ApiErrorMappers put(@NonNull Class<E> exceptionType, @NonNull Function<E, ApiErrorMessage> mapper) {
            mappers.put(exceptionType, mapper);
            return this;
        }

        private <E extends Exception> Optional<ApiErrorMessage> lookupApiErrorMessage(@NonNull E exceptionType) {
            //noinspection unchecked
            Function<E, ApiErrorMessage> mapper = (Function<E, ApiErrorMessage>) mappers.get(exceptionType.getClass());
            if (mapper != null) {
                return of(mapper.apply(exceptionType));
            } else {
                return empty();
            }
        }
    }
}
