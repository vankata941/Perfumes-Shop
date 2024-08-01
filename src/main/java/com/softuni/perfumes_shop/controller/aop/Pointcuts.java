package com.softuni.perfumes_shop.controller.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("@annotation(WarnExecutionExceeds)")
    void onWarnExecutionExceeds() {}
}
