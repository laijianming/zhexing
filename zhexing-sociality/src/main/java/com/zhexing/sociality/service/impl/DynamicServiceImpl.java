package com.zhexing.sociality.service.impl;

import com.google.gson.Gson;
import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.dao.DynamicDao;
import com.zhexing.sociality.dao.RedisDao;
import com.zhexing.sociality.enums.SocialEnum;
import com.zhexing.sociality.pojo.Dynamic;
import com.zhexing.sociality.pojo.Tag;
import com.zhexing.sociality.service.DynamicService;
import com.zhexing.sociality.service.TagService;
import com.zhexing.sociality.utils.JsonUtils;
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
        String[] tname;
        if(tnames == null){
            tname = new String[0]; // 包含的话题
        }else {
            tname = tnames.split(" "); // 包含的话题
        }
        ArrayList<Tag> list = new ArrayList<>();
        for(int i = 0; i < tname.length; i ++){
            if(tagService.searchTag(tname[i]) == null){ // 数据库查找是否有此话题，若无则加入到list中，待会批量添加
                list.add(new Tag(null,tname[i],dynamic.getUserId(),null,new Date(),1L));
            }
        }
        // 调用TagService创建话题
        if(list.size() != 0)
            tagService.publishTag(list);

        // 2、发布动态 -> 将动态持久化
        dynamic.setPublishTime(new Date());
        Long aLong = dynamicDao.publishDynamic(dynamic);

        // 3、将动态存到redis缓存（设置30分钟有效期），并将动态返回给前端页面
        if(aLong >= 1) { // 插入数据库成功后将其添加到缓存
            addDynamicCache(dynamic);
            // 添加 话题下动态排行缓存
            for(int i = 0 ; i < tname.length ; i ++){
                redisDao.zadd(SocialEnum.TAG_DYNAMIC_ + "" + tname[i],dynamic.getDynamicId() + ""); // 默认查找次数 1
            }
        }
        return ZheXingResult.ok(dynamic);
    }


    /**
     * 将动态添加到缓存
     * @param dynamic
     */
    public void addDynamicCache(Dynamic dynamic){
        // 添加动态缓存
        redisDao.put(SocialEnum.DYNAMIC_ + "" + dynamic.getDynamicId(), JsonUtils.objectToJson(dynamic), 30 * 60 * 1000L);

    }

    /**
     * 删除动态
     * @param dynamicId
     * @param tnames 该动态包含的话题
     * @return
     */
    @Override
    public ZheXingResult deleteDynamid(Long dynamicId,String tnames) {
        // 1、删除数据库中该动态
        dynamicDao.deleteDynamic(dynamicId);

        // 2、删除缓存中该动态
        redisDao.deleteCache(SocialEnum.DYNAMIC_ + "" + dynamicId);

        // 3、删除 话题下动态排行的记录
        String[] ts = tnames.split(" ");
        for(int i = 0; i < ts.length; i ++){
            redisDao.zdel(SocialEnum.TAG_DYNAMIC_ + ts[i],dynamicId + "");
        }

        // 4、返回删除成功
        return ZheXingResult.ok();
    }

    /**
     * 热搜话题下动态查找
     * @param tname 话题名
     * @param start 开始的条数
     * @param nums 查出多少条
     * @return
     */
    @Override
    public ZheXingResult hotDynamic(String tname,int start,int nums) {

        // 将该话题的热度计数 增加1
        redisDao.zincrby(SocialEnum.HOT_TAG_COUNT_ + "",tname,1);

        // 1、查redis缓存中热点动态缓存 start开始 nums条数的 dynamicId
        Set set = redisDao.zrevrange(SocialEnum.TAG_DYNAMIC_ + tname, start, nums, false);
        Iterator iterator = set.iterator();
        ArrayList<String> results = new ArrayList<>();
        while (iterator.hasNext()){  // 通过dynamicId 查找缓存中或数据库中的动态信息
            Object dynamicId = iterator.next();  // dynamicId 是该 动态 id
            // 1.1、从缓存中查找相应的动态缓存数据
            String result = redisDao.get(dynamicId + "");
            // 1.2、若缓存中未找到，则从数据库中查找
            if(result.equals("null")){
                long l = Long.parseLong("" + dynamicId);
                Dynamic dynamic = dynamicDao.selectById(l);
                // 添加缓存
                addDynamicCache(dynamic);
                result = JsonUtils.objectToJson(dynamic);
            }
            // 1.3、给话题下动态排行查询次数 +1
            redisDao.zincrby(SocialEnum.TAG_DYNAMIC_ + tname,dynamicId+"",1);

            // 1.4、给该条动态缓存添加1分钟的过期时间
            redisDao.updateExpire(SocialEnum.DYNAMIC_ + "" + dynamicId,1L * 60 * 1000);
            results.add(result);
        }

        // 3、返回查到的数据
        return ZheXingResult.ok(results);
    }

    /**
     * 推荐动态查找
     * @return
     */
    @Override
    public ZheXingResult recommend() {
//        redisDao.

        return null;
    }

    /**
     * 动态点赞处理
     * @param userId
     * @param dynamicId
     * @param tnames
     * @param flag 1 表示 点赞； 0（其他）表示取消点赞
     * @return
     */
    @Override
    public ZheXingResult likeDynamic(Long userId, Long dynamicId,String tnames,int flag) {
        Long sadd;
        double count;
        if(flag == 1){
            // 添加点赞缓存，另有定时任务会去持久化
            sadd = redisDao.sadd(SocialEnum.LIKE_DYNAMIC_ + "" + dynamicId, userId + "");
            // 加入持久化队列
            redisDao.sadd(SocialEnum.DURABLE_DYNAMIC_ + "",dynamicId + "");
            count = 1;
        }else {
            // 取消点赞，删除对应缓存
            sadd = redisDao.srem(SocialEnum.LIKE_DYNAMIC_ + "" + dynamicId, userId + "");
            count = -1;
        }
        // 给该话题下动态热度排行计数 + count 值 和 话题排行计数 + count/5 值
        if (sadd != 0){
            if(tnames != null && !tnames.equals("")) {
                String[] split = tnames.split(" ");
                for (int i = 0; i < split.length; i++) {
                    redisDao.zincrby(SocialEnum.TAG_DYNAMIC_ + split[i], dynamicId + "", count);
                    redisDao.zincrby(SocialEnum.HOT_TAG_COUNT_ + "",split[i],count/5);
                }
            }
        }
        return ZheXingResult.ok(sadd);
    }



}
