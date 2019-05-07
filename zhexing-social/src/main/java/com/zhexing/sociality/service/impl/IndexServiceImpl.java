package com.zhexing.sociality.service.impl;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.dao.FollowDao;
import com.zhexing.sociality.dao.UserDao;
import com.zhexing.sociality.service.DynamicService;
import com.zhexing.sociality.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
