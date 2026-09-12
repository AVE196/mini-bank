package ru.ave;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.ave.config.AppConfig;

public class App {

    public static void main() {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);


    }

}
