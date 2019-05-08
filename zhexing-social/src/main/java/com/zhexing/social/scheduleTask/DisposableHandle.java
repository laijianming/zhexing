package com.zhexing.social.scheduleTask;

import com.zhexing.social.pojo.WsUser;
import com.zhexing.social.websocket.MessageWebsocket;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class DisposableHandle implements DisposableBean,ExitCodeGenerator {


    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public void destroy() throws Exception {
        System.out.println("=======================================================");
        System.out.println("容器退出 调用了自定义的销毁方法");
        // 将本服务在redis上的用户在线记录删除
        ConcurrentHashMap<Long, WsUser> usersMap = MessageWebsocket.usersMap;
        String WSUser = MessageWebsocket.WS_USER;
        for(Long key : usersMap.keySet()){
            redisTemplate.opsForHash().delete(WSUser,key + "");
        }
        System.out.println("=======================================================");
    }

    @Override
    public int getExitCode() {
        return 3;
    }



}
