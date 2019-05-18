package com.zhexing.social.controller;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.social.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  个人主页
 */
@RestController
public class IndexController {


    @Autowired
    IndexService indexService;


    /**
     * 获取个人主页
     * @param uname
     * @param userId
     * @return
     */
    @GetMapping("/index/person")
    public ZheXingResult index(String uname,Long userId) {
        return indexService.personIndex(uname,userId);

    }


    /**
     * 查询用户 uname和unickname模糊查找
     * @param userId
     * @param user
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/search/getusers")
    public ZheXingResult getUser(Long userId,String user,Long start,Long end){
        return indexService.searchUsers(userId,user, start, end);
    }


    /**
     * 推荐好友
     * @return
     */
    @GetMapping("/recommend/user")
    public ZheXingResult recommendUser(Long userId){
        return null;
    }

}
