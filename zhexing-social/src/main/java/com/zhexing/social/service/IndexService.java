package com.zhexing.social.service;

import com.zhexing.common.resultPojo.ZheXingResult;

/**
 *  个人主页信息
 */
public interface IndexService {

    /**
     * 个人主页
     * @param uanme 主页用户id
     * @return
     */
    ZheXingResult personIndex(String uanme,Long userId);


    /**
     * 模糊查询用户信息
     * @param user uname unickname
     * @param start
     * @param end
     * @return
     */
    ZheXingResult searchUsers(Long userId,String user,Long start,Long end);


    ZheXingResult recommendUser(Long userId);

}
