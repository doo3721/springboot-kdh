package net.likelion.bebc25.spring.componentscan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class Driver {

//    // 필드에 붙이는게 제일 깔끔함, 다만 권장하지는 않음
//    @Autowired
    private Car car;

    Driver() {
        System.out.println("기본 생성자 호출");
    }

    // 의존성 주입(DI) 어노테이션, 생성자가 하나만 있을 경우에는 생략 가능
    @Autowired
    Driver(@Qualifier("gasolineCar") Car car) {
        System.out.println("Called Constructor Injection: " + car);
        this.car = car;
    }

//    // setter로도 가능
//    @Autowired
//    public void setCar(Car car) {
//        System.out.println("Setter Injection 호출");
//        this.car = car;
//    }

    public void driveCar(int maxSpeed) {
        car.startEngine();
        car.drive();
        car.stopEngine();
    }
}
