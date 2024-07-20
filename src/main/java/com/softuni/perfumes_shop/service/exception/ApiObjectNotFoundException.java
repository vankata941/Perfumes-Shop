package com.softuni.perfumes_shop.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApiObjectNotFoundException extends RuntimeException {

    public ApiObjectNotFoundException(String message) {
        super(message);
    }
}
