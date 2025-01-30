package com.example.chapter.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AopForTransaction {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        joinPoint.proceed();
    }
}
