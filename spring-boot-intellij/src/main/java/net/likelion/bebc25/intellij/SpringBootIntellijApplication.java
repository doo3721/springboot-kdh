package net.likelion.bebc25.intellij;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

// SpringBootApplication에 AOP를 위한 어노테이션들이 미리 선언되어 있다
@SpringBootApplication
public class SpringBootIntellijApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =  SpringApplication.run(SpringBootIntellijApplication.class, args);
        Driver driver = context.getBean(Driver.class);
        driver.driveCar(150);
        context.close();
    }

}
