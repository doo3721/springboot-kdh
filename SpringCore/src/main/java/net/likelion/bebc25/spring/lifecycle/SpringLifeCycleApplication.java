package net.likelion.bebc25.spring.lifecycle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringLifeCycleApplication {
    void main() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);) {
//            TempFilesSupport support = context.getBean(TempFilesSupport.class);
            TempFilesSupport2 support = context.getBean(TempFilesSupport2.class);
            support.writeLog("사용자가 로그인 됨");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//            TempFilesSupport support = context.getBean(TempFilesSupport.class);
//            support.writeLog("사용자가 로그인 됨");
//            // 소멸 메소드를 불러와야 @PreDestroy가 작동됨
//            context.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
