package com.example.chapter.aop;

import com.example.chapter.util.CustomSpringELParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final RedissonClient redisson;

    @Around("@annotation(com.example.chapter.aop.RedissonLock)")
    public void redissonLock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock annotation = method.getAnnotation(RedissonLock.class);

        String lockKey = method.getName() + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), annotation.value());

        RLock lock = redisson.getLock(lockKey);

        try {
            boolean lockable = lock.tryLock(annotation.waitTime(), annotation.leaseTime(), TimeUnit.MICROSECONDS);
            if (!lockable) {
                log.warn("Lock 획득 실패:{}", lockKey);
                return;
            }
            joinPoint.proceed();
        } catch (InterruptedException e) {
            throw new RuntimeException("에러 발생:" + e.getMessage());
        } finally {
            if (lock.isHeldByCurrentThread()) {
                log.info("Lock 해제:{}", lockKey);
                lock.unlock();
            }
        }
    }
}
