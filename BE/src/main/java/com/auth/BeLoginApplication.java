package com.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.auth")
public class BeLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeLoginApplication.class, args);
    }
}
