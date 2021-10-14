package com.nicolatesser.nevernote.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

@ExtendWith(MockitoExtension.class)
class RestResponseEntityExceptionHandlerTest {

    private static final String MESSAGE = "message";
    private static final RuntimeException EXCEPTION = new RuntimeException(MESSAGE);

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private RestResponseEntityExceptionHandler restResponseEntityExceptionHandler;

    @Test
    void handleNotFound() {
        //given
        ResponseEntity<Object> notFound = ResponseEntity.notFound().build();

        //when
        ResponseEntity<Object> responseEntity = restResponseEntityExceptionHandler.handleNotFound(EXCEPTION,
            webRequest);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(notFound.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(
            String.format("The resource you where looking for couldn´t be found: %s", MESSAGE));
    }

    @Test
    void handleBadRequest() {
        //given
        ResponseEntity<Object> badRequest = ResponseEntity.badRequest().build();

        //when
        ResponseEntity<Object> responseEntity = restResponseEntityExceptionHandler.handleBadRequest(EXCEPTION,
            webRequest);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(badRequest.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(
            String.format("The request is invalid: %s", MESSAGE));
    }

    @Test
    void handleOtherExceptions() {
        //given
        ResponseEntity<Object> internalError = ResponseEntity.internalServerError().build();

        //when
        ResponseEntity<Object> responseEntity = restResponseEntityExceptionHandler.handleOtherExceptions(EXCEPTION,
            webRequest);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(internalError.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(
            String.format("Internal server error: %s", MESSAGE));
    }
}