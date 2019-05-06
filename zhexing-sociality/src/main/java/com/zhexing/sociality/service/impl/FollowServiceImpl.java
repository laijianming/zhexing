package com.zhexing.sociality.service.impl;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.dao.FollowDao;
import com.zhexing.sociality.pojo.Follow;
import com.zhexing.sociality.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements FollowService {


    @Autowired
    FollowDao followDao;

    /**
     * 关注处理
     * @param follow
     * @param flag true表示关注，false表示取消关注
     * @return
     */
    @Override
    public ZheXingResult followUser(Follow follow, boolean flag) {
        if(flag){
            followDao.followUser(follow);
        }else {
            followDao.cancelFollow(follow);
        }
        return ZheXingResult.ok();
    }

    /**
     * 查找所有关注的人
     * @param userId 用户id
     * @return
     */
    @Override
    public ZheXingResult searchAllFollow(Long userId) {
        return ZheXingResult.ok(followDao.searchAllFollow(userId));
    }

    /**
     * 查找所有被关注的人
     * @param follower 被关注的用户id
     * @return
     */
    @Override
    public ZheXingResult searchAllFollowedUser(Long follower) {
        return ZheXingResult.ok(followDao.searchAllFollowedUser(follower));
    }
}
