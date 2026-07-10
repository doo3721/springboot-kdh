package net.likelion.bebc25.spring.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class TempFilesSupport {
    // Value로 값 지정
    @Value("resources/temp.log")
    private String filePath;

    public TempFilesSupport() {
        System.out.println("기본 생성자 호출: " + filePath);
    }

    // 네트워크, 파일시스템 연결 같은 무거운 작업을 수행하는 초기화 메소드 정의
    // 비즈니스 메소드 호출 전에 실행해야 할 작업
    @PostConstruct  // 초기화 콜백
    public void init() {
        System.out.println(filePath + " 경로의 FileOutputStream 생성");
    }

    public void writeLog(String msg) {
        System.out.println(filePath + " 에 로그 저장: " + msg);
    }

    @PreDestroy // 소멸 콜백
    public void close() {
        System.out.println(filePath + " 경로의 FileOutputStream을 소멸");
    }
}
