package com.zhexing.zhexingzuul.controller.zhexingSocialController;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.zhexingzuul.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取消息历史记录
 */
@RestController
public class MsgController {


    @Autowired
    SocialService socialService;


    /**
     * 获取历史消息
     * @param userId
     * @return
     */
    @GetMapping("/user/message")
    public ZheXingResult getMessage(Long userId){
        return socialService.getMessage(userId);
    }

    /**
     * 删除历史消息
     * @param key
     * @return
     */
    @DeleteMapping("/user/message")
    public ZheXingResult delMessage(String key){
        return socialService.delMessage(key);
    }

}
