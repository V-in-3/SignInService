package com.backend.service.sign.in.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;

@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@Documented
@Parameter(
        in = ParameterIn.HEADER,
        name = "X-RequestId",
        required = true,
        example = "a028c465-ce48-45b3-854f-2d20f362c492",
        description = "Unique identifier of request"
)
public @interface ApiHeaderRequestId {
}
