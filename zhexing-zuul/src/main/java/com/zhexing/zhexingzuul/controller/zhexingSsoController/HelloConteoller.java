package com.zhexing.zhexingzuul.controller.zhexingSsoController;

import com.zhexing.zhexingzuul.service.SsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloConteoller {

    @Autowired
    SsoService ssoService;

    @RequestMapping("/hello")
    public String hello(){
        return ssoService.hello();
    }

}
