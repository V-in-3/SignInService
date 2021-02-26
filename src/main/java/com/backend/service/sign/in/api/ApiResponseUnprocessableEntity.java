package com.backend.service.sign.in.api;

import com.backend.service.sign.in.web.exception.ApiErrorMessage;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;

@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@Documented
@ApiResponse(
        responseCode = "422",
        description = "",
        content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))
)
public @interface ApiResponseUnprocessableEntity {

    String value();
}
