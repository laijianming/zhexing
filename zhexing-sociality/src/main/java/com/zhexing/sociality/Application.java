package com.zhexing.sociality;

import com.zhexing.sociality.dao.RedisDao;
import com.zhexing.sociality.enums.DynamicEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Iterator;
import java.util.Set;

@SpringBootApplication
public class Application {

    @Autowired
    static RedisDao redisDao;

    public static void main(String[] args) {
        // 开启一个定时任务，定时往热点缓存减少热点
        // 减少每一个热点数据的
        redisCacheClear();

        SpringApplication.run(Application.class, args);
    }


    public static void redisCacheClear(){
        Set zrevrange = redisDao.zrevrange(DynamicEnum.HOT_DYNAMIC_DYNAMICID_ + "", 0, -1, false);
        Iterator iterator = zrevrange.iterator();
        while (iterator.hasNext()){
            Object next = iterator.next();
//            redisDao
            redisDao.zincrby(DynamicEnum.HOT_DYNAMIC_DYNAMICID_ + "",next + "",-10);
        }

    }

}
