package net.likelion.bebc25.spring.aop.staticproxy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringStaticProxyApplication {
    void main() {
        // 스프링 컨테이너 생성 (Bean 정보 분석을 위한 Config 객체 지정)
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // driver Bean을 컨테이너에서 꺼냄
        Driver driver = context.getBean(Driver.class);

        // 비즈니스 로직 실행
        driver.driveCar();

        // 컨테이너 종료 및 리소스 반환
        ((AnnotationConfigApplicationContext)context).close();
    }
}
