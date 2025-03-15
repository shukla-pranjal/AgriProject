package com.icwd.config.server.agri_eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class AgriEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgriEurekaApplication.class, args);
    }

}
