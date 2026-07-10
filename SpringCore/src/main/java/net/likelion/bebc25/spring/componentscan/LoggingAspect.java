package net.likelion.bebc25.spring.componentscan;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

// 횡단 관심사 클래스 정의
@Aspect
@Component
public class LoggingAspect {
//    @Pointcut("execution(* net.likelion.bebc25.spring.aop.springaop.*Car.*(..))")
    @Pointcut("execution(* net.likelion.bebc25.spring.componentscan.Driver.*(..))")
    private void springAopPackageMethods() {}

    @Before("springAopPackageMethods()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("[AOP 로그 before] 메소드 실행 전에 처리할 코드를 작성합니다.");
        Object[] args = joinPoint.getArgs();  // 메소드의 매개변수를 가져올 수 있다
        System.out.println(Arrays.toString(args));
    }

    @After("springAopPackageMethods()")
    public void logAfter() {
        System.out.println("[AOP 로그 after] 메소드 실행 후에 처리할 코드를 작성합니다.");
    }

    @Around("springAopPackageMethods()")
    public void logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("[AOP 로그 around] 메소드 실행 전에 처리할 코드를 작성합니다.");
        joinPoint.proceed();
        System.out.println("[AOP 로그 around] 메소드 실행 후에 처리할 코드를 작성합니다.");
    }
}
