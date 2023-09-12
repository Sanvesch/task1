package com.example.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class UserControllerLoggingAspect {

    private Logger logger = LoggerFactory.getLogger(UserControllerLoggingAspect.class);

    @Before("execution(* your.package.controller.UserController.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        logger.info("Entering: " + joinPoint.getSignature().toShortString());
        
    }

    @After("execution(* your.package.controller.UserController.*(..))")
    public void logMethodExit(JoinPoint joinPoint) {
        logger.info("Exiting: " + joinPoint.getSignature().toShortString());
       
    }
}

