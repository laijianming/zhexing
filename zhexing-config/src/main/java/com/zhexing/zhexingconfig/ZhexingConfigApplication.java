package com.zhexing.zhexingconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ZhexingConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhexingConfigApplication.class, args);
    }

}
