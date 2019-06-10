package com.zhexing.zhexingeureka01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ZhexingEureka01Application {

    public static void main(String[] args) {
        SpringApplication.run(ZhexingEureka01Application.class, args);
    }

}
