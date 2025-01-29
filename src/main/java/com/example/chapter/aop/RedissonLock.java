package com.example.chapter.aop;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedissonLock {

    String value(); // 락 이름
    long leaseTime() default 2000L; // 락 점유 최대 시간
    long waitTime() default 5000L; // 락 대기 최대 시간
}
