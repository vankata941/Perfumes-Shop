package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.service.exception.AuthorizationCheckException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthorizationCheckException.class)
    public String handleAuthorizationCheckException(AuthorizationCheckException e) {

        return "page-not-found";
    }
}
