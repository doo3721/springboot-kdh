package net.likelion.bebc25.spring.aop.dynamicproxy;

public class GasolineCar implements Car {
    @Override
    public void startEngine() {
        System.out.println("가솔린 엔진에 점화하여 시동을 켭니다");
    }

    @Override
    public void drive() {
        System.out.println("가솔린을 사용하여 주행합니다");
    }

    @Override
    public void stopEngine() {
        System.out.println("가솔린 공급을 차단하여 시동을 끕니다");
    }
}
