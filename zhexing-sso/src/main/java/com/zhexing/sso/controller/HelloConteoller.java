package com.zhexing.sso.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloConteoller {


    @RequestMapping("/hello")
    public String hello(){
        return "hello world!!!";
    }

}
