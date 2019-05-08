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

    @Override
    public ZheXingResult searchUsers(Long userId,String user,Long start,Long end){
        List<Long> userIds = userDao.selectUsersByUserIdOrUname(user + "%", user + "%", start, end);
        List result = new ArrayList();
        for(int i = 0,len = userIds.size() ;i < len ; i++){
            result.add(followDao.selectUserFollowInfo(userIds.get(i),userId));

        }
        return ZheXingResult.ok();
    }
}
