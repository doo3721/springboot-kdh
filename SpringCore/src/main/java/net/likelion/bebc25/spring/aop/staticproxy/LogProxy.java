package net.likelion.bebc25.spring.aop.staticproxy;

public class LogProxy implements Car {
    private final Car target;

    public LogProxy(Car target) {
        this.target = target;
    }

    @Override
    public void startEngine() {
        System.out.println("[startEngine 메소드 실행 전] 엔진을 점검한다");
        this.target.startEngine();
    }

    @Override
    public void drive() {
        System.out.println("[drive 메소드 실행 전] 안전벨트를 맵니다");
        this.target.drive();
    }

    @Override
    public void stopEngine() {
        System.out.println("[stopEngine 메소드 실행 전] 안전벨트를 풉니다");
        this.target.stopEngine();
        System.out.println("[stopEngine 메소드 실행 후] 하차합니다");
    }
}
