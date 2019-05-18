package com.zhexing.zhexingzuul.controller.zhexingSocialController;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.zhexingzuul.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class IPController {

    @Autowired
    SocialService socialService;

    /**
     * 获取当前服务器的IP和端口
     * @return
     */
    @GetMapping("/websocket/getip")
    public ZheXingResult getIPPort(){
        return socialService.getIPPort();
    }



}
