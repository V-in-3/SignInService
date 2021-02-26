package com.backend.service.sign.in.controller;

import com.backend.service.sign.in.dto.EmailResponse;
import com.backend.service.sign.in.service.SignInService;
import com.backend.service.sign.in.web.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.backend.service.sign.in.Constants.CHECK;
import static com.backend.service.sign.in.Constants.ROUTE_SIGN_IN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignInController.class)
@ComponentScan({"com.backend.service.sign.in"})
public class SignInControllerTest {

    public static final String X_REQUEST_ID = "X-RequestId";
    public static final String REQUEST_ID = "a028c465-ce48-45b3-854f-2d20f362c492";

    @MockBean
    private SignInService signInService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sendOtpSuccess() throws Exception {
        UUID id = UUID.randomUUID();

        when(signInService.sendOtp(any()))
                .thenReturn(new EmailResponse(id));

        mockMvc.perform(post(ROUTE_SIGN_IN)
                .headers(commonRequestIdHeader())
                .content(bodyForSendOtp())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(id.toString())))
                .andExpect(header().string(X_REQUEST_ID, REQUEST_ID));
    }

    @Test
    void sendOtpBadRequestMissingHeader() throws Exception {

        mockMvc.perform(post(ROUTE_SIGN_IN)
                // .headers(commonRequestIdHeader())
                .content(bodyForSendOtp())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Mandatory header X-RequestId does not exist in the request")))
                .andExpect(jsonPath("$.path", equalTo("/sign-in")));
    }

    @Test
    void sendOtpUnprocessableEntity() throws Exception {
        //language=JSON
        String json = "{\n" +
                "  \"email\": null\n" +
                "}";

        mockMvc.perform(post(ROUTE_SIGN_IN)
                .headers(commonRequestIdHeader())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", equalTo(422)))
                .andExpect(jsonPath("$.error", equalTo("Unprocessable Entity")))
                .andExpect(jsonPath("$.message", equalTo("validation.failed")))
                .andExpect(jsonPath("$.path", equalTo("/sign-in")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[*].object", contains("emailRequest")))
                .andExpect(jsonPath("$.errors[*].property", contains("email")))
                .andExpect(jsonPath("$.errors[*].message", contains("must not be null or empty")))
                .andExpect(header().string(X_REQUEST_ID, REQUEST_ID));
    }

    @Test
     void sendOtpInternalServerError() throws Exception {
        doThrow(ApiException.internalServerError("send.email.fail"))
                .when(signInService).sendOtp(any());

        mockMvc.perform(post(ROUTE_SIGN_IN)
                .headers(commonRequestIdHeader())
                .content(bodyForSendOtp())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", equalTo(500)))
                .andExpect(jsonPath("$.error", equalTo("Internal Server Error")))
                .andExpect(jsonPath("$.message", equalTo("send.email.fail")))
                .andExpect(jsonPath("$.path", equalTo("/sign-in")))
                .andExpect(header().string(X_REQUEST_ID, REQUEST_ID));
    }

    @Test
    void checkOtpSuccess() throws Exception {

        doNothing().when(signInService).checkOtp(any());

        mockMvc.perform(post(ROUTE_SIGN_IN + CHECK)
                .headers(commonRequestIdHeader())
                .content(bodyForCheckOtp())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andExpect(header().string(X_REQUEST_ID, REQUEST_ID));
    }

    @Test
    void checkOtpBadRequestMissingHeader() throws Exception {

        mockMvc.perform(post(ROUTE_SIGN_IN + CHECK)
                // .headers(commonRequestIdHeader())
                .content(bodyForCheckOtp())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Mandatory header X-RequestId does not exist in the request")))
                .andExpect(jsonPath("$.path", equalTo("/sign-in/otp")));
    }

    @Test
    void checkOtpBadRequestOtpIsIncorrect() throws Exception {
        doThrow(ApiException.badRequest("otp.incorrect"))
                .when(signInService).checkOtp(any());

        mockMvc.perform(post(ROUTE_SIGN_IN + CHECK)
                .headers(commonRequestIdHeader())
                .content(bodyForCheckOtp())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("otp.incorrect")))
                .andExpect(jsonPath("$.path", equalTo("/sign-in/otp")))
                .andExpect(header().string(X_REQUEST_ID, REQUEST_ID));
    }

    @Test
    void checkOtpNotFoundRecordNotFound() throws Exception {
        doThrow(ApiException.notFound("record.not-found"))
                .when(signInService).checkOtp(any());

        mockMvc.perform(post(ROUTE_SIGN_IN + CHECK)
                .headers(commonRequestIdHeader())
                .content(bodyForCheckOtp())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.error", equalTo("Not Found")))
                .andExpect(jsonPath("$.message", equalTo("record.not-found")))
                .andExpect(jsonPath("$.path", equalTo("/sign-in/otp")))
                .andExpect(header().string(X_REQUEST_ID, REQUEST_ID));
    }

    @Test
    void checkOtpUnprocessableEntity() throws Exception {
        //language=JSON
        String json = "{\n" +
                "  \"id\": null,\n" +
                "  \"otp\": \"\"\n" +
                "}";

        mockMvc.perform(post(ROUTE_SIGN_IN + CHECK)
                .headers(commonRequestIdHeader())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", equalTo(422)))
                .andExpect(jsonPath("$.error", equalTo("Unprocessable Entity")))
                .andExpect(jsonPath("$.message", equalTo("validation.failed")))
                .andExpect(jsonPath("$.path", equalTo("/sign-in/otp")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[*].object", containsInAnyOrder("otpRequest", "otpRequest")))
                .andExpect(jsonPath("$.errors[*].property", containsInAnyOrder("id", "otp")))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder("must not be null", "must not be null or empty")))
                .andExpect(header().string(X_REQUEST_ID, REQUEST_ID));
    }

    @Test
    void checkOtpInternalServerError() throws Exception {
        doThrow(ApiException.internalServerError("Internal Server Error"))
                .when(signInService).checkOtp(any());

        mockMvc.perform(post(ROUTE_SIGN_IN + CHECK)
                .headers(commonRequestIdHeader())
                .content(bodyForCheckOtp())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", equalTo(500)))
                .andExpect(jsonPath("$.error", equalTo("Internal Server Error")))
                .andExpect(jsonPath("$.message", equalTo("Internal Server Error")))
                .andExpect(jsonPath("$.path", equalTo("/sign-in/otp")))
                .andExpect(header().string(X_REQUEST_ID, REQUEST_ID));
    }

    private String bodyForSendOtp() {
        //language=JSON
        return "{\n" +
                "  \"email\": \"vvvorotnik@gmail.com\"\n" +
                "}";
    }

    private String bodyForCheckOtp() {
        //language=JSON
        return "{\n" +
                "  \"id\": \"e9e87c05-82eb-4522-bc47-f0fcfdde4cab\",\n" +
                "  \"otp\": \"3185\"\n" +
                "}";
    }

    private HttpHeaders commonRequestIdHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(X_REQUEST_ID, REQUEST_ID);

        return headers;
    }
}