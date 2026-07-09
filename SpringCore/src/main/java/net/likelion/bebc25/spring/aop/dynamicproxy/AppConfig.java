package net.likelion.bebc25.spring.aop.dynamicproxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;


@Configuration
public class AppConfig {
    @Bean
    public Car car() {
        Car target = new HybridCar();

        // LogProxy와 같은 프록시 클래스를 동적으로 생성한다
        Car proxyCar = (Car)Proxy.newProxyInstance(
                Car.class.getClassLoader(),             // 클래스 로더
                new Class[]{ Car.class },               // 구현할 인터페이스 목록
                new TimeCheckInvocationHandler(target)  // 로직을 구현한 핸들러
        );

        return proxyCar;
    }

    @Bean
    public Driver driver(Car car) {
        return new Driver(car);
    }
}
