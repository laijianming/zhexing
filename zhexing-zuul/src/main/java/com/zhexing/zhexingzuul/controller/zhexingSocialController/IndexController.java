package com.zhexing.zhexingzuul.controller.zhexingSocialController;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.zhexingzuul.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  个人主页
 */
@RestController
public class IndexController {


    @Autowired
    SocialService socialService;


    /**
     * 获取个人主页
     * @param uname
     * @param userId
     * @return
     */
    @GetMapping("/index/person")
    public ZheXingResult index(String uname,Long userId) {
        return socialService.index(uname,userId);

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
        return socialService.getUser(userId,user, start, end);
    }


    /**
     * 推荐好友
     * @return
     */
    @GetMapping("/recommend/user")
    public ZheXingResult recommendUser(Long userId){
        return socialService.recommendUser(userId);
    }

}
