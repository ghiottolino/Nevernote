package com.nicolatesser.nevernote.rest;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler
    extends ResponseEntityExceptionHandler {

    //TODO: return json error objects instead than plain strings.

    @ExceptionHandler(value = {EmptyResultDataAccessException.class, ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(
        RuntimeException ex, WebRequest request) {
        String bodyOfResponse = String.format("The resource you where looking for couldn´t be found: %s",
            ex.getMessage());
        return handleExceptionInternal(ex, bodyOfResponse,
            new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleBadRequest(
        RuntimeException ex, WebRequest request) {
        String bodyOfResponse = String.format("The request is invalid: %s",
            ex.getMessage());
        return handleExceptionInternal(ex, bodyOfResponse,
            new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {Throwable.class})
    protected ResponseEntity<Object> handleOtherExceptions(
        RuntimeException ex, WebRequest request) {
        log.error("Unmapped exception: ", ex);
        String bodyOfResponse = String.format("Internal server error: %s",
            ex.getMessage());
        return handleExceptionInternal(ex, bodyOfResponse,
            new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
