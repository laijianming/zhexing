package com.zhexing.sociality.service.impl;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.dao.RedisDao;
import com.zhexing.sociality.dao.TagDao;
import com.zhexing.sociality.enums.TagEnum;
import com.zhexing.sociality.pojo.Tag;
import com.zhexing.sociality.service.TagService;
import com.zhexing.sociality.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

        // 2、把新建的话题添加到缓存中,并设置有效期30分钟
        for(int i = 0; i < tags.size(); i ++){
            redisDao.put(TagEnum.TAG_NAME_ + "" + tags.get(i).getTname(),JsonUtils.objectToJson(tags.get(i)),30 * 60 * 1000L);
        }
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
}
