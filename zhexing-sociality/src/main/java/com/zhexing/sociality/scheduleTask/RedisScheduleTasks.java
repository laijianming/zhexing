package com.zhexing.sociality.scheduleTask;


import com.zhexing.sociality.dao.RedisDao;
import com.zhexing.sociality.enums.SocialEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Component
@EnableScheduling // 1、开启定时任务
@EnableAsync // 2、开启异步
public class RedisScheduleTasks {

    @Autowired
    RedisDao redisDao;

    int count = 0;

//    @Async
//    @Scheduled(fixedDelay = 1000)  //间隔1秒
    public void first() throws InterruptedException {
        System.out.println("第一个定时任务开始 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
        System.out.println();
        Thread.sleep(1000 * 10);
    }

//    @Async
//    @Scheduled(fixedDelay = 2000)
    public void second() {
        System.out.println("第二个定时任务开始 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
        System.out.println();
    }

    /**
     * 开启热搜计数计算
     *  查出这五分钟内
     * @throws InterruptedException
     */
    @Async
    @Scheduled(fixedDelay =  60 * 1000)  //间隔30秒刷新一次
    public void hotCalculate() {
        System.out.println("热搜计数计算: 时间==> " + LocalDateTime.now().toLocalTime() + "   || 次数==> " + ++count);
        System.out.println("开始处理热搜 === >");
        long start = new Date().getTime();
        // 获取热搜排行计数的前20条话题
        Set hotTags = redisDao.zrevrange(SocialEnum.HOT_TAG_COUNT_ + "", 0, 20, false);
        if(hotTags.size() == 0){
            long end = new Date().getTime();
            System.out.println("处理提前热搜完毕 === >  耗时 == " + (end - start) + " 毫秒");
            System.out.println();
            return;
        }
        Object[] objects = hotTags.toArray();
        String[] array = new String[objects.length];
        for(int i = 0 ; i < objects.length; i ++){
            array[i] = objects[i] + "";
        }

        // 1、删除话题排行的缓存，然后将新查出来的热搜话题添加进去
        redisDao.deleteCache(SocialEnum.HOT_TAG_ + "");
        redisDao.putAllList(SocialEnum.HOT_TAG_ + "",null,array);

        // 2、将查出的话题排行在变成热度 20 - 1 的范围添加回话题排行计数中
        for(int i = 0; i < array.length; i ++){
            redisDao.zadd(SocialEnum.HOT_TAG_COUNT_ + "",array[i],20.0 - i);
        }
        long end = new Date().getTime();
        System.out.println("处理热搜完毕 === >  耗时 == " + (end - start));
        System.out.println();
    }

    // 2、 每天 凌晨3点持久化一次动态点赞及其他点赞 （Set结构缓存）DURABLE_DYNAMIC_ , dynamicId

}
