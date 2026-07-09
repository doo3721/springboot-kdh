package net.likelion.bebc25.spring.aop.dynamicproxy;

public class Driver {
    private Car car;

    // 의존성 주입 (DI, Dependency Injection)
    Driver(Car car) {
        this.car = car;
    }

    public void driveCar() {
        car.startEngine();
        car.drive();
        car.stopEngine();
    }
}
