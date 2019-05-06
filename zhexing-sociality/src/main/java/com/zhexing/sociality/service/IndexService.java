package com.zhexing.sociality.service;

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

}
