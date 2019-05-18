package com.zhexing.social.service.impl;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.social.dao.FollowDao;
import com.zhexing.social.dao.UserDao;
import com.zhexing.social.service.DynamicService;
import com.zhexing.social.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class IndexServiceImpl implements IndexService {


    @Autowired
    DynamicService dynamicService;

    @Autowired
    UserDao userDao;

    @Autowired
    FollowDao followDao;

    /**
     * 个人主页信息查询
     * @param uname 主页用户name
     * @return
     */
    @Override
    public ZheXingResult personIndex(String uname,Long userId) {
        // 1、去数据库查uname用户的id
        Long indexId = userDao.selectUserIdByUname(uname);
        // 2、去dynamicService查该用户的动态 查10条  -- 暂不做
        return ZheXingResult.ok(followDao.selectUserFollowInfo(indexId,userId));
    }

    /**
     *
     * @param userId
     * @param user uname unickname
     * @param start
     * @param end
     * @return
     */
    @Override
    public ZheXingResult searchUsers(Long userId,String user,Long start,Long end){
        List<Long> userIds = userDao.selectUsersByUserIdOrUname(user + "%", user + "%", start, end);
        List result = new ArrayList();
        Long userId1;
        for(int i = 0,len = userIds.size() ;i < len ; i++){
            userId1 = userIds.get(i);
            result.add(followDao.selectUserFollowInfo(userId1,userId));

        }
        return ZheXingResult.ok(result);
    }

    /**
     * 推荐好友  实现：推荐自己关注的人的关注 若不够三个，则推荐热度比较高的几个用户
     * @param userId
     * @return
     */
    @Override
    public ZheXingResult recommendUser(Long userId){

        return ZheXingResult.ok();
    }
}
