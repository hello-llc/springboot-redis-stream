package com.llc.redis.method;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MethodApplication {

    public static void main(String[] args) {
        SpringApplication.run(MethodApplication.class, args);
    }

}
