package net.likelion.bebc25.spring.di.setter;

public class Driver {
    private Car car;

    // setter injection
    public void setCar(Car car) {
        this.car = car;
    }

    public void driveCar() {
        car.startEngine();
        car.drive();
        car.stopEngine();
    }
}
