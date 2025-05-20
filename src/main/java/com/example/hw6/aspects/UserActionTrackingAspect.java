package com.example.hw6.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
public class UserActionTrackingAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserActionTrackingAspect.class);

    @Around("@annotation(TrackUserAction)")
    public Object trackUserAction(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        TrackUserAction annotation = method.getAnnotation(TrackUserAction.class);

        // Получаем описание действия
        String actionDescription = annotation.value().isEmpty()
                ? method.getName()
                : annotation.value();

        // Логируем начало действия
        logger.info("Action '{}' started at {}", actionDescription, LocalDateTime.now());

        try {
            // Выполняем метод
            Object result = joinPoint.proceed();

            // Логируем успешное завершение
            logger.info("Action '{}' completed successfully at {}",
                    actionDescription, LocalDateTime.now());

            return result;
        } catch (Exception e) {
            // Логируем ошибку
            logger.error("Action '{}' failed at {}: {}",
                    actionDescription, LocalDateTime.now(), e.getMessage());
            throw e;
        }
    }
}