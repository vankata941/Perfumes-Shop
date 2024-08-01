package com.softuni.perfumes_shop.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AuthorizationCheckException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "You are not authorized to view this page!";

    public AuthorizationCheckException() {
        super(DEFAULT_MESSAGE);
    }
}
