package com.zhexing.social.service;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.social.pojo.Follow;

/**
 *  关注业务处理接口
 */
public interface FollowService {

    /**
     * 关注处理
     * @param follow
     * @param flag true表示关注，false表示取消关注
     * @return
     */
    ZheXingResult followUser(Follow follow,boolean flag);


    /**
     * 查找所有关注的人
     * @param userId 用户id
     * @return
     */
    ZheXingResult searchAllFollow(Long userId);

    /**
     * 查找所有被关注的人
     * @param followedUser 被关注的用户id
     * @return
     */
    ZheXingResult searchAllFollowedUser(Long followedUser);


}
