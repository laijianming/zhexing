package com.zhexing.zhexingzuul.controller.zhexingSocialController;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.zhexingzuul.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 话题处理controller
 */
@RestController
public class TagController {

    @Autowired
    SocialService socialService;

    /**
     * 获取热搜话题
     * @return
     */
    @GetMapping("/tag/hotTag")
    public ZheXingResult hotTag(){
        return socialService.hotTag();
    }

    /**
     * 查找话题
     * @param tname
     * @return
     */
    @GetMapping("/tag/searchTag")
    public ZheXingResult searchTag(String tname){
        return socialService.searchTag(tname);
    }


}
