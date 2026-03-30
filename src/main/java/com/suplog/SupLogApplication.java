package com.suplog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SupLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupLogApplication.class, args);
    }

}
