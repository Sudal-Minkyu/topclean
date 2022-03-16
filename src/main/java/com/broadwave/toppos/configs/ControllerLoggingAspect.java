package com.broadwave.toppos.configs;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author InSeok
 * Date : 2022-03-16
 * Remark :
 */
@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {
    @Around("execution(* com.broadwave..*RestController.*(..))")
    public Object restControllerLogging(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object result = pjp.proceed();
        stopWatch.stop();

        log.info("[RestController AOP Logging] {}.{} :: {} ms",className,methodName,stopWatch.getTotalTimeMillis());
        return result;
    }
}
