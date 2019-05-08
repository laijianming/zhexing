package com.zhexing.social.controller;

import com.zhexing.common.resultPojo.ZheXingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class IPController {



    /**
     * 获取当前服务器的IP和端口
     * @param request
     * @return
     */
    @GetMapping("/websocket/getip")
    public ZheXingResult getIPPort(HttpServletRequest request){
        new Thread(new Runnable() {
            @Override
            public void run() {


            }
        }).start();
        return ZheXingResult.ok(request.getLocalAddr() + ":" + request.getLocalPort());
    }



}
