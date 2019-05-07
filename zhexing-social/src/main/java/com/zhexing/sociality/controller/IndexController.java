package com.zhexing.sociality.controller;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  个人主页
 */
@RestController
public class IndexController {


    @Autowired
    IndexService indexService;

    @GetMapping("/index/person")
    public ZheXingResult index(String uname,Long userId) {
        System.out.println(uname + " ===================" + userId);
        return indexService.personIndex(uname,userId);

    }


}
