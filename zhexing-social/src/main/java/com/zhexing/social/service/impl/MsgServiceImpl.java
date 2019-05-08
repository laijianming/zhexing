package com.zhexing.social.service.impl;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.social.dao.RedisDao;
import com.zhexing.social.service.MsgService;
import com.zhexing.social.websocket.MessageWebsocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *  用户消息处理
 */
@Service
public class MsgServiceImpl implements MsgService {

    @Autowired
    RedisDao redisDao;


    @Override
    public ZheXingResult getMessage(Long userId) {
        String WSMSG = MessageWebsocket.WS_MSG;
        Set keys1 = redisDao.keys(WSMSG + userId + "_*");
        Object[] k1 = keys1.toArray();
        List result = new ArrayList();
        List msg;
        Map map = new HashMap();
        for(int i = 0,len = k1.length; i < len; i ++){
            msg = redisDao.lrange(k1[i] + "", 0, -1);
//            result.add(msg);
            map.put(k1[i],msg);
        }
        return ZheXingResult.ok(map);
    }

    @Override
    public ZheXingResult delMessage(String key) {
        redisDao.deleteCache(key);
        return ZheXingResult.ok();
    }
}
