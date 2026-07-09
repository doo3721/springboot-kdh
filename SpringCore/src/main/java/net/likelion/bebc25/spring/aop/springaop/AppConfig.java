package net.likelion.bebc25.spring.aop.springaop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Configuration
// 스프링 컨테이너에 @Aspect 어노테이션이 붙은 bean들을 찾아서 프록시 처리를 하도록 지시
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public Car car() {
        return new HybridCar();
    }

    @Bean
    public Driver driver(Car car) {
        return new Driver(car);
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
