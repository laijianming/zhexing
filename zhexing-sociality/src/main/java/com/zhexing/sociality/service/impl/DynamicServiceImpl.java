package com.zhexing.sociality.service.impl;

import com.google.gson.Gson;
import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.dao.DynamicDao;
import com.zhexing.sociality.dao.RedisDao;
import com.zhexing.sociality.enums.DynamicEnum;
import com.zhexing.sociality.pojo.Dynamic;
import com.zhexing.sociality.pojo.DynamicSet;
import com.zhexing.sociality.pojo.Tag;
import com.zhexing.sociality.service.DynamicService;
import com.zhexing.sociality.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *  动态业务处理service
 */
@Service
public class DynamicServiceImpl implements DynamicService {

    @Autowired
    DynamicDao dynamicDao;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    TagService tagService;

    @Autowired
    RedisDao redisDao;


    /**
     *  发布动态
     * @param dynamic 动态pojo
     * @return 发布的动态
     */
    @Override
    public ZheXingResult publishDynamic(Dynamic dynamic) {
        // 1、处理话题，把不存在的话题先创建
        String tnames = dynamic.getTnames();
        String[] tname = tnames.split(" ");
        ArrayList<Tag> list = new ArrayList<>();
        for(int i = 0; i < tname.length; i ++){
            if(tagService.searchTag(tname[i]) == null){ // 数据库查找是否有此话题，若无则加入到list中，待会批量添加
                list.add(new Tag(null,tname[i],dynamic.getUserId(),null,new Date()));
            }
        }
        // 调用TagService创建话题
        if(list.size() != 0)
            tagService.publishTag(list);

        // 2、发布动态 -> 将动态持久化
        dynamic.setPublishTime(new Date());
        Long aLong = dynamicDao.publishDynamic(dynamic);

        // 3、将动态存到redis缓存（设置30分钟有效期），并将动态返回给前端页面
        if(aLong >= 1) { // 插入数据库成功
            // 添加动态缓存
            redisDao.put(DynamicEnum.DYNAMIC_DYNAMICID_ + "" + dynamic.getDynamicId(), new Gson().toJson(dynamic), 30 * 60 * 1000L);
            // 添加热搜排行缓存
            redisDao.zadd(DynamicEnum.HOT_DYNAMIC_DYNAMICID_ + "",dynamic.getDynamicId() + "");
        }
        return ZheXingResult.ok(dynamic);
    }

    /**
     *  删除动态
     * @param dynamicId
     * @return
     */
    @Override
    public ZheXingResult deleteDynamid(Long dynamicId) {
        // 1、删除数据库中该动态
        dynamicDao.deleteDynamic(dynamicId);

        // 2、删除缓存中该动态
        redisDao.deleteCache(DynamicEnum.DYNAMIC_DYNAMICID_ + "" + dynamicId);

        // 3、删除热搜缓存计数中的记录
        redisDao.zdel(DynamicEnum.HOT_DYNAMIC_DYNAMICID_ + "",DynamicEnum.DYNAMIC_DYNAMICID_ + "" + dynamicId);
        // 4、返回删除成功
        return ZheXingResult.ok();
    }

    /**
     *  热搜动态查找
     * @return
     */
    @Override
    public ZheXingResult hotDynamic() {

        // 1、查redis缓存中热点动态缓存前10条的 dynamicId
        Set set = redisDao.zrevrange(DynamicEnum.HOT_DYNAMIC_DYNAMICID_ + "", 0, 9, false);
        // 1.1、查完之后给查出来的动态热度值各+0.05
        Iterator iterator = set.iterator();
        while (iterator.hasNext()){
            Object next = iterator.next();
            redisDao.zincrby(DynamicEnum.HOT_DYNAMIC_DYNAMICID_ + "",next + "",0.05);
        }
        // 1.2、再从缓存中查找相应的动态缓存数据

        // 2、若缓存中未找到则从数据库中查找

        // 3、返回查到的数据

        return null;
    }


}
