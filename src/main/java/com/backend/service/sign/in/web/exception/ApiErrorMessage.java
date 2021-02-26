package com.backend.service.sign.in.web.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * Unified error message container which is rendered as:
 * <br/>
 * <pre>
 * {
 *    "timestamp": "2017-10-20T13:36:04.859Z",
 *    "status": 400,
 *    "error": "Bad request",
 *    "message": "Validation failed",
 *    "path": "/path",
 *    "errors": [
 *       {
 *          "object": "Validation",
 *          "property": "conditions[].value1",
 *          "message": "conditions[].value1 must be at least 0!",
 *          "invalidValue": -10
 *       },
 *       {
 *          "object": "Validation",
 *          "property": "conditions[].value2",
 *          "message": "conditions[].value2 must be at least 0!",
 *          "invalidValue": -20
 *       }
 *    ]
 * }
 * </pre>
 */
@Schema(
        name = "ApiError",
        description = "API error object",
        example = "{\n" +
                "   \"timestamp\": \"2017-10-20T13:36:04.859Z\",\n" +
                "   \"status\": 400,\n" +
                "   \"error\": \"Bad request\",\n" +
                "   \"message\": \"Error message example\",\n" +
                "   \"path\": \"/path\"\n" +
                "}"
)
@JsonInclude(NON_EMPTY)
@JsonPropertyOrder({"timestamp", "status", "error", "message", "path", "errors"})
@Data
public class ApiErrorMessage implements Serializable {

    /**
     * Error's timestamp
     */
    private final Instant timestamp;

    @JsonIgnore
    private final HttpStatus httpStatus;

    /**
     * Error's HTTP status
     */
    private final Integer status;

    /**
     * Error's status description
     */
    private final String error;

    /**
     * Error's detailed message
     */
    @NonNull
    private final String message;

    /**
     * Request path related to the error
     */
    private final String path;

    /**
     * Collection of detailed sub-errors, for example, validation error of the request body specific field
     */
    @Schema(nullable = true, description = "Array with optional error descriptions")
    Collection<Error> errors = new ArrayList<>();

    /**
     * Make it public for using in tests
     */
    public ApiErrorMessage(
            @NonNull Instant timestamp,
            @NonNull HttpStatus httpStatus,
            @NonNull String message,
            @NonNull String path
    ) {
        this.timestamp = timestamp;
        this.httpStatus = httpStatus;
        this.status = this.httpStatus.value();
        this.error = this.httpStatus.getReasonPhrase();
        this.message = message;
        this.path = path;
    }

    /**
     * Add {@link Error} to errors collection
     *
     * @param error must not be null
     */
    public void addError(@NonNull Error error) {
        errors.add(error);
    }

    /**
     * Sub-error container
     */
    @Schema(description = "Optional error description")
    @JsonInclude(NON_EMPTY)
    @Value
    public static class Error implements Serializable {

        /**
         * Object name related to the sub-error
         */
        @Schema(nullable = true, description = "Name of the object contains incorrect property")
        String object;

        /**
         * Object property name related to the sub-error
         */
        @Schema(nullable = true, description = "Incorrect property name")
        String property;

        /**
         * Detailed sub-error message
         */
        @Schema(description = "Additional error description")
        @NonNull String message;

        /**
         * Invalid value of object property related to the sub-error
         */
        @Schema(nullable = true, description = "Invalid property value", type = "string")
        Object invalidValue;
    }
}
