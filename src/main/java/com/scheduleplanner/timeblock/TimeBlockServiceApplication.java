package com.scheduleplanner.timeblock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TimeBlockServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeBlockServiceApplication.class, args);
    }
} 