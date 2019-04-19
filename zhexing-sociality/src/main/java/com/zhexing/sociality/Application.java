package com.zhexing.sociality;

import com.zhexing.sociality.dao.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {

    @Autowired
    static RedisDao redisDao;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }



}
