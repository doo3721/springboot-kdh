package net.likelion.bebc25.spring.di.setter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Car car() {
        return new HybridCar();
    }

    @Bean
    public Driver driver(Car car) {
        Driver driver = new Driver();
        driver.setCar(car);
        return driver;
    }
}
