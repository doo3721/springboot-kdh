package net.likelion.bebc25.spring.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TempFilesSupport2 implements InitializingBean, DisposableBean {
    // Value로 값 지정
    @Value("resources/temp.log")
    private String filePath;

    public TempFilesSupport2() {
        System.out.println("기본 생성자 호출: " + filePath);
    }

    // 초기화 콜백의 스프링 버전
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(filePath + " 경로의 FileOutputStream 생성");
    }

    public void writeLog(String msg) {
        System.out.println(filePath + " 에 로그 저장: " + msg);
    }

    // 소멸 콜백의 스프링 버전
    @Override
    public void destroy() throws Exception {
        System.out.println(filePath + " 경로의 FileOutputStream을 소멸");
    }
}
