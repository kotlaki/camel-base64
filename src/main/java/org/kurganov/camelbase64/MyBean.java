package org.kurganov.camelbase64;

import org.springframework.stereotype.Component;

@Component
public class MyBean {
    public void myMethod(String body) throws Exception {
        System.out.println("Получено: " + body);
    }

}
