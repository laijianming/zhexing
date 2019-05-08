package com.zhexing.social.service;

import com.zhexing.common.resultPojo.ZheXingResult;

public interface MsgService {



    ZheXingResult getMessage(Long userId);

    ZheXingResult delMessage(String key);

}
