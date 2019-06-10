package com.zhexing.zhexingeureka02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ZhexingEureka02Application {

    public static void main(String[] args) {
        SpringApplication.run(ZhexingEureka02Application.class, args);
    }

}
