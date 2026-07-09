package net.likelion.bebc25.spring.aop.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TimeCheckInvocationHandler implements InvocationHandler {
    private final Car target;

    TimeCheckInvocationHandler(Car target) {
        this.target = target;
    }

    @Override
    // target의 모든 메소드에 대해서 호출함
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("[동적 프록시] 메소드 실행 전: " + method.getName());

        long s = System.currentTimeMillis();
        // target의 메소드 호출
        Object result = method.invoke(target, args);
        long e = System.currentTimeMillis();

        System.out.println("[동적 프록시] 메소드 실행 후: " + method.getName() + " [시간: " + (e - s) + "ms]");

        return result;
    }


}
