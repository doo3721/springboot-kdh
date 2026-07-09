package net.likelion.bebc25.spring.aop.springaop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringAopApplication {
    void main() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Driver driver = context.getBean(Driver.class);

        System.out.println("Driver 객체: " + driver);

        driver.driveCar(150);

        ((AnnotationConfigApplicationContext)context).close();
    }
}
