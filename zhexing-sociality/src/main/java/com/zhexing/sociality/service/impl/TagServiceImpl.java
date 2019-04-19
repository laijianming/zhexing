package com.zhexing.sociality.service.impl;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.dao.RedisDao;
import com.zhexing.sociality.dao.TagDao;
import com.zhexing.sociality.enums.SocialEnum;
import com.zhexing.sociality.pojo.Tag;
import com.zhexing.sociality.service.TagService;
import com.zhexing.sociality.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *  话题业务处理service
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagDao tagDao;

    @Autowired
    RedisDao redisDao;

    /**
     * 批量创建话题
     * @param tags 话题
     * @return
     */
    @Override
    public ZheXingResult publishTag(ArrayList<Tag> tags) {
        // 1、创建话题并添加到数据库中
        tagDao.createTags(tags);

        // 2、把新建的话题添加到缓存中
        String[] V = new String[tags.size()];
        for(int i = 0; i < tags.size(); i ++){
            V[i] = tags.get(i).getTname();
            // 3、把话题添加到话题排行计数中
            redisDao.zadd(SocialEnum.HOT_TAG_COUNT_ + "",V[i]);
        }
        redisDao.sadd(SocialEnum.TAGS_ + "",V);


        return ZheXingResult.ok();
    }


    /**
     * 先查找缓存中该话题；缓存中没有则再查数据库中该话题；
     *  缓存中若有该
     * @param tname 话题名
     * @return
     */
    @Override
    public Tag searchTag(String tname) {
        // 1、在缓存中查该话题是否存在，存在则增加2分钟有效期并返回

        // 2、若缓存中没有该话题，则查数据库，若数据库存在，则添加缓存并设置30分钟有效期

        // 3、否则让调用方封装好数据再进行创建，返回null
        return tagDao.search(tname);
    }

    /**
     * 热搜话题查找 并返回该话题下有多少条动态
     * @return
     */
    @Override
    public ZheXingResult hotTags() {

        // 1、查找话题排行中的前10条数据
        List lrange = redisDao.lrange(SocialEnum.HOT_TAG_ + "", 0, 10);
        // 2、查出每个话题下有多少条动态
        Tag[] tags = new Tag[lrange.size()];
        for(int i = 0; i < lrange.size(); i ++){
            String tname = lrange.get(i) + "";
            Long zcard = redisDao.zcard(SocialEnum.TAG_DYNAMIC_ + tname);
            tags[i] = new Tag();
            tags[i].setTname(tname);
            tags[i].setDynamics(zcard);
        }
        return ZheXingResult.ok(tags);
    }
}
