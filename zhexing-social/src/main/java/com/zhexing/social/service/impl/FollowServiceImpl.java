package com.zhexing.social.service.impl;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.social.dao.FollowDao;
import com.zhexing.social.pojo.Follow;
import com.zhexing.social.pojo.FollowUser;
import com.zhexing.social.pojo.IndexUser;
import com.zhexing.social.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            if(followDao.cancelFollow(follow) == 0)
                followDao.followUser(follow);
            else return ZheXingResult.build(501,"您已关注过对方！！");
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
        List<FollowUser> followUsers = followDao.searchAllFollow(userId);
        for(FollowUser followUser : followUsers){
            supplementInfo(followUser,userId);
        }
        return ZheXingResult.ok(followUsers);
    }

    /**
     * 查找所有被关注的人
     * @param follower 被关注的用户id
     * @return
     */
    @Override
    public ZheXingResult searchAllFollowedUser(Long follower) {
        List<FollowUser> followUsers = followDao.searchAllFollowedUser(follower);
        for(FollowUser followUser : followUsers){
            supplementInfo(followUser,follower);
        }
        return ZheXingResult.ok(followUsers);
    }

    /**
     * 补全该对象的关注数、被关注数、动态数
     * @param followUser
     */
    public void supplementInfo(FollowUser followUser,Long follower){
        IndexUser indexUser = followDao.selectUserFollowInfo(followUser.getUserId(), follower);
        followUser.setUname(indexUser.getUname());
        followUser.setUnickname(indexUser.getUnickname());
        followUser.setUchathead(indexUser.getUchathead());
        followUser.setFollowCount(indexUser.getFollowCount());
        followUser.setFollowerCount(indexUser.getFollowerCount());
        followUser.setDynamicCount(indexUser.getDynamicCount());
        followUser.setFollowing(indexUser.getFollowing());
        followUser.setFollower(indexUser.getFollower());
    }

}
