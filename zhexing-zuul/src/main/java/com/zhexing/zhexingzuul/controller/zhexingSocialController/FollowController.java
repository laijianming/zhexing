package com.zhexing.zhexingzuul.controller.zhexingSocialController;

import com.zhexing.common.pojo.Follow;
import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.zhexingzuul.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FollowController {

    @Autowired
    SocialService socialService;

    /**
     * 关注处理
     * @param follow
     * @param flag true表示关注，false表示取消关注
     * @return
     */
    @PostMapping("/follow/follow")
    ZheXingResult followUser(Follow follow, boolean flag){
        return socialService.followUser(follow, flag);
    }

    /**
     * 查看某人关注的所有人
     * @param userId
     * @return
     */
    @GetMapping("/follow/following")
    ZheXingResult searchAllFollow(Long userId){
        return socialService.searchAllFollow(userId);
    }


    /**
     * 查看某人的粉丝
     * @param follower
     * @return
     */
    @GetMapping("/follow/follower")
    ZheXingResult searchAllFollowedUser(Long follower){
        return socialService.searchAllFollowedUser(follower);
    }

}
