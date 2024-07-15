package com.softuni.perfumes_shop.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AuthorizationCheckException extends RuntimeException {

    public AuthorizationCheckException() {
        super();
    }
}
