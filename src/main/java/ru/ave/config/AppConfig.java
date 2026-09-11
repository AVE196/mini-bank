package ru.ave.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ru.ave")
@PropertySource("classpath:application.properties")
public class AppConfig {
}
