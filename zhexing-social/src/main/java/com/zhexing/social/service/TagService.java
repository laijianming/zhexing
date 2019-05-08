package com.zhexing.social.service;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.social.pojo.Tag;

import java.util.ArrayList;

/**
 *  话题service
 */
public interface TagService {

    /**
     * 发布话题
     *  该方法会由 DynamicServiceImpl的publishDynamic方法调用
     *  注：发布话题只能在发布动态时创建
     * @param tags 话题
     * @return
     */
    ZheXingResult publishTag(ArrayList<Tag> tags);

    /**
     * 查找话题
     * @param tname 话题名
     * @return
     */
    Tag searchTag(String tname);


    /**
     * 热搜话题查找
     * @return
     */
    ZheXingResult hotTags();

}
