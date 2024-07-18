package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.service.exception.AuthorizationCheckException;
import com.softuni.perfumes_shop.service.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthorizationCheckException.class)
    public String handleAuthorizationCheckException(AuthorizationCheckException e, Model model) {

        model.addAttribute("message", e.getMessage());
        return "page-not-found";
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public String handleObjectNotFoundException(ObjectNotFoundException e, Model model) {

        model.addAttribute("message", e.getMessage());
        return "page-not-found";
    }
}
