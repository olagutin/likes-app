package com.likesapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class LikesAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(LikesAppApplication.class, args);
    }

}
