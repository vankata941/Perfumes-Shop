package com.softuni.perfumes_shop.controller.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

@Aspect
@Component
public class MonitoringAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringAspect.class);

    @Around("Pointcuts.onWarnExecutionExceeds()")
    Object monitorExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        WarnExecutionExceeds annotation = getAnnotation(joinPoint);

        long threshold = annotation.threshold();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();

        stopWatch.stop();
        long executionTime = stopWatch.lastTaskInfo().getTimeMillis();

        if (executionTime > threshold) {
            LOGGER.warn("Execution time exceeded by: {} ms", executionTime - threshold);
        }

        return result;
    }

    private static WarnExecutionExceeds getAnnotation(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();

        return method.getAnnotation(WarnExecutionExceeds.class);
    }
}
