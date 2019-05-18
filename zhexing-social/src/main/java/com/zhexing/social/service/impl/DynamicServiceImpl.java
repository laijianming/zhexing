package com.zhexing.social.service.impl;

import com.zhexing.common.pojo.DynUser;
import com.zhexing.common.pojo.Dynamic;
import com.zhexing.common.pojo.Image;
import com.zhexing.common.pojo.Tag;
import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.social.dao.DynamicDao;
import com.zhexing.social.dao.RedisDao;
import com.zhexing.social.dao.UserDao;
import com.zhexing.social.enums.SocialEnum;
import com.zhexing.social.service.DynamicService;
import com.zhexing.social.service.TagService;
import com.zhexing.social.utils.JsonUtils;
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

    @Autowired
    UserDao userDao;


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

        // 3、将动态id记录进缓存中
        redisDao.lpush(SocialEnum.DYNAMIC_ + "",dynamic.getDynamicId() + "");

        // 4、将动态存到redis缓存（设置30分钟有效期），并将动态返回给前端页面
        if(aLong >= 1) { // 插入数据库成功后将其添加到缓存
            addDynamicCache(dynamic);
            // 添加 话题下动态排行缓存
            for(int i = 0 ; i < tname.length ; i ++){
                redisDao.zadd(SocialEnum.TAG_DYNAMIC_ + ":" + tname[i],dynamic.getDynamicId() + ""); // 默认查找次数 1
            }
        }
        lcCount(dynamic,dynamic.getDynamicId(),dynamic.getUserId());
        return ZheXingResult.ok(dynamic);
    }


    /**
     * 将动态添加到缓存
     * @param dynamic
     */
    public void addDynamicCache(Dynamic dynamic){
        // 添加动态缓存
        redisDao.put(SocialEnum.DYNAMIC_ + ":" + dynamic.getDynamicId(), JsonUtils.objectToJson(dynamic), 30 * 60 * 1000L);

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
        redisDao.deleteCache(SocialEnum.DYNAMIC_ + ":" + dynamicId);

        // 3、删除 话题下动态排行的记录
        String[] ts = tnames.split(" ");
        for(int i = 0; i < ts.length; i ++){
            redisDao.zdel(SocialEnum.TAG_DYNAMIC_ + ":" + ts[i],dynamicId + "");
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
    public ZheXingResult hotDynamic(String tname,int start,int nums,Long userId) {

        // 将该话题的热度计数 增加1
        redisDao.zincrby(SocialEnum.HOT_TAG_COUNT_ + "",tname,1);

        // 1、查redis缓存中热点动态缓存 start开始 nums条数的 dynamicId
        Set set = redisDao.zrevrange(SocialEnum.TAG_DYNAMIC_ + ":" + tname, start, nums, false);
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
                // 添加动态缓存
                redisDao.lpush(SocialEnum.DYNAMIC_ + "",dynamicId + "");
            }

            // 1.2.5、添加点赞数和评论数
            Dynamic dynamic = JsonUtils.jsonToPojo(result, Dynamic.class);
            lcCount(dynamic,Long.parseLong(dynamicId + ""),userId);

            // 补充发动态者信息
            supplementUserInfo(dynamic);

            // 1.3、给话题下动态排行查询次数 +1
            redisDao.zincrby(SocialEnum.TAG_DYNAMIC_ + ":" + tname,dynamicId+"",1);

            // 1.4、给该条动态缓存添加1分钟的过期时间
            redisDao.updateExpire(SocialEnum.DYNAMIC_ + ":" + dynamicId,1L * 60 * 1000);


            results.add(JsonUtils.objectToJson(dynamic));
        }

        // 3、返回查到的数据
        return ZheXingResult.ok(results);
    }

    /**
     * 补充发动态用户的信息
     * @param dynamic
     */
    public void supplementUserInfo(Dynamic dynamic){
        List<DynUser> dynUsers = userDao.selectById(dynamic.getUserId());
        if(dynUsers.size() == 0) return;
        DynUser dynUser = dynUsers.get(0);
        dynamic.setUname(dynUser.getUname());
        dynamic.setUnickname(dynUser.getUnickname());
        dynamic.setUchathead(dynUser.getUchathead());

    }


    /**
     * 推荐动态查找
     * @param start
     * @param end
     * @return
     */
    @Override
    public ZheXingResult recommend(Long start, Long end,Long userId) {
        // 缓存中找userId 的dynamicId
        List lrange = redisDao.lrange(SocialEnum.DYNAMIC_ + "", start, end);
        // 数据库再找一次的dynamicId
        List<Long> longs = dynamicDao.selectDynamicIdByUserIdtrue(start, end);


        Set<Long> dynamicIdsSet = new HashSet<>(longs);
        if(lrange != null)
            for(int i = 0,len = lrange.size(); i < len; i ++){
                dynamicIdsSet.add(Long.parseLong(lrange.get(i) + ""));
            }

        Object[] dynamicIds = dynamicIdsSet.toArray();
        // 根据上面查的id来查找动态
        ArrayList<Dynamic> list = new ArrayList<>();
        for(int i = 0,len = dynamicIds.length; i < len; i ++){
            Dynamic dynamic = searchDynamic(dynamicIds[len - 1 - i] + "");
            Long dynamicId = dynamic.getDynamicId();
            // 封装好每个动态的点赞和评论数
            lcCount(dynamic,dynamicId,userId);

            // 补充发动态者信息
            supplementUserInfo(dynamic);

            list.add(dynamic);
        }

        return ZheXingResult.ok(list);
    }

    /**
     * 封装动态是否点赞，点赞数，评论数  并处理图片信息
     * lcCount : l like c comment
     * @param dynamic
     * @param dynamicId
     * @param userId 当前用户id
     * @return
     */
    public void lcCount(Dynamic dynamic,Long dynamicId,Long userId){
        dynamic.setAction(redisDao.sismember(SocialEnum.LIKE_DYNAMIC_ + ":" + dynamicId,userId + ""));
        dynamic.setLikesCount(redisDao.scard(SocialEnum.LIKE_DYNAMIC_ + ":" + dynamicId));
        dynamic.setCommentsCount(redisDao.hlen(SocialEnum.DYNAMIC_COMMENT_ + ":" + dynamicId));
        // 封装图片信息
        String images = dynamic.getImages();
        String mimages = dynamic.getMimages();
        if(images != null && !images.equals("") && mimages != null && !mimages.equals("")){
            String[] src = images.split(" ");
            String[] msrc = mimages.split(" ");
            dynamic.setImgList(new ArrayList<>());
            for(int i = 0,len = src.length; i < len; i ++){
                dynamic.getImgList().add(new Image(src[i],msrc[i]));
            }
        }
    }


    /**
     * 根据 动态id来查找动态
     * @param dynamicId
     * @return
     */
    Dynamic searchDynamic(String dynamicId){
        // 1、先查缓存中是否有该动态，有则返回
        String s = redisDao.get(SocialEnum.DYNAMIC_ + ":" + dynamicId);
        if(s != null && !s.equals("null"))
            return JsonUtils.jsonToPojo(s,Dynamic.class);
        // 2、查数据库中该动态
        return dynamicDao.selectById(Long.parseLong(dynamicId));
    }

    /**
     * 查看已关注人的动态
     * @param userId 当前用户id
     * @param start 开始的条数
     * @param end 查多少条
     * @return
     */
    @Override
    public ZheXingResult allFollowDynamics(Long userId, Long start, Long end) {
        // 去缓存中查该用户关注的人  === 暂不做缓存

        // 1、查询关注的用户有哪些
        List<Long> followed_userId = userDao.selectFollowUserByUserId(userId);
        // 查数据库中包含这些userId的动态并按发布动态时间逆序排序（给最新的动态）， limit start,end 来查找
        // SELECT * FROM tb_dynamic WHERE user_id IN (99,88,71,2) ORDER BY publish_time DESC LIMIT 4,2
        List<Dynamic> dynamics;
        if(followed_userId.size() != 0){
            for(Object o : followed_userId){
                System.out.println("关注的id ==" + o);
            }
            dynamics = dynamicDao.selectDynamicByUserIds(followed_userId, start, end);
            // 封装动态的其他信息
            ArrayList result = new ArrayList();
            for(Dynamic dynamic : dynamics){
                supplementUserInfo(dynamic);
                lcCount(dynamic,dynamic.getDynamicId(),userId);
                result.add(dynamic);
            }
            return ZheXingResult.ok(result);
        }
        return ZheXingResult.ok(new ArrayList<>());

    }

    /**
     * 查找某人的动态
     * @param uname 关注的人的uname
     * @param start 开始的条数
     * @param end 查多少条
     * @return
     */
    @Override
    public ZheXingResult followDynamic(Long userId,String uname, Long start, Long end) {
        // 1、去缓存中查该用户的动态id -- 暂不做缓存

        // 2、查数据库该用户所有的动态的动态id
        List<Long> dynamicIds = dynamicDao.selectDynamicIdByUserId(uname,start,end);
        // 3、根据动态id查找动态
        if(dynamicIds.size() == 0){
            return ZheXingResult.ok(new ArrayList<>());
        }
        List<Dynamic> dynamics = dynamicDao.selectDynamicByIds(dynamicIds);
        // 封装动态相关信息
        for(Dynamic dynamic : dynamics){
            supplementUserInfo(dynamic);
            lcCount(dynamic,dynamic.getDynamicId(),userId);
        }
        // 4、返回查找结果
        return ZheXingResult.ok(dynamics);
    }


    /**
     * 动态点赞处理
     * @param userId
     * @param dynamicId
     * @param tnames
     * @param flag true 表示 点赞；
     * @return
     */
    @Override
    public ZheXingResult likeDynamic(Long userId, Long dynamicId,String tnames,boolean flag) {
        Long sadd;
        double count;
        if(flag){
            // 添加点赞缓存，另有定时任务会去持久化
            sadd = redisDao.sadd(SocialEnum.LIKE_DYNAMIC_ + ":" + dynamicId, userId + "");
            // 加入持久化队列
            redisDao.sadd(SocialEnum.DURABLE_DYNAMIC_ + "",dynamicId + "");
            count = 1;
        }else {
            // 取消点赞，删除对应缓存
            sadd = redisDao.srem(SocialEnum.LIKE_DYNAMIC_ + ":" + dynamicId, userId + "");
            count = -1;
        }
        // 给该话题下动态热度排行计数 + count 值 和 话题排行计数 + count/5 值
        if (sadd != 0){
            if(tnames != null && !tnames.equals("")) {
                String[] split = tnames.split(" ");
                for (int i = 0; i < split.length; i++) {
                    redisDao.zincrby(SocialEnum.TAG_DYNAMIC_ + ":" + split[i], dynamicId + "", count);
                    redisDao.zincrby(SocialEnum.HOT_TAG_COUNT_ + "",split[i],count/5);
                }
            }
        }
        return ZheXingResult.ok(sadd);
    }

    /**
     * 转发动态
     * @param dynamic
     * @return
     */
    @Override
    public ZheXingResult forwardDynamic(Dynamic dynamic) {
        return null;
    }

    /**
     * 收藏动态
     * @param dynamicId
     * @return
     */
    @Override
    public ZheXingResult collectDynamic(Long dynamicId) {
        return null;
    }


}
