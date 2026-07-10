package net.likelion.bebc25.spring.componentscan;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component(/*value=*/"hCar")  // bean의 이름을 hCar로 지정
@Primary  // 동일 타입의 여러 bean 중에 메인으로 지정
public class HybridCar implements Car {
    @Override
    public void startEngine() {
        System.out.println("시스템 전원을 켜서 하이브리드 시동을 켭니다");
    }

    @Override
    public void drive() {
        System.out.println("가솔린과 전기를 사용하여 주행합니다");
    }

    @Override
    public void stopEngine() {
        System.out.println("하이브리드 시스템 종료 처리를 합니다");
    }
}
