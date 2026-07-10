package net.likelion.bebc25.intellij;

import org.springframework.stereotype.Component;

@Component
public class Driver {
    private Car car;

    Driver(Car car) {
        System.out.println("Called Constructor Injection: " + car);
        this.car = car;
    }

    public void driveCar(int maxSpeed) {
        car.startEngine();
        car.drive();
        car.stopEngine();
    }
}
