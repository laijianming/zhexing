package com.zhexing.social.controller;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.social.dao.RedisDao;
import com.zhexing.social.service.MsgService;
import com.zhexing.social.websocket.MessageWebsocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;
import java.util.*;

/**
 * 获取消息历史记录
 */
@RestController
public class MsgController {


    @Autowired
    MsgService msgService;


    /**
     * 获取历史消息
     * @param userId
     * @return
     */
    @GetMapping("/user/message")
    public ZheXingResult getMessage(Long userId){
        return msgService.getMessage(userId);
    }

    /**
     * 删除历史消息
     * @param key
     * @return
     */
    @DeleteMapping("/user/message")
    public ZheXingResult delMessage(String key){
        return msgService.delMessage(key);
    }

}
