package net.likelion.bebc25.intellij;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j  // lombok의 로깅 관련 어노테이션
public class LoggingAspect {
    @Pointcut("execution(* net.likelion.bebc25.intellij.*Driver.*(..))")
    private void springAopPackageMethods() {}

    @Before("springAopPackageMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[AOP 로그 before] 메소드 실행 전에 처리할 코드를 작성합니다.");
    }

    @After("springAopPackageMethods()")
    public void logAfter() {
        log.info("[AOP 로그 after] 메소드 실행 후에 처리할 코드를 작성합니다.");
    }

    @Around("springAopPackageMethods()")
    public void logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("[AOP 로그 around] 메소드 실행 전에 처리할 코드를 작성합니다.");
        joinPoint.proceed();
        log.debug("[AOP 로그 around] 메소드 실행 후에 처리할 코드를 작성합니다.");
    }
}
