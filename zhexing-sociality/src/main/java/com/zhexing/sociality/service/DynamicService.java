package com.zhexing.sociality.service;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.pojo.Dynamic;


/**
 *  动态业务接口
 */
public interface DynamicService {


    /**
     *  发布动态
     * @param dynamic
     * @return
     */
    ZheXingResult publishDynamic(Dynamic dynamic);

    /**
     *  删除动态
     * @param dynamicId
     * @return
     */
    ZheXingResult deleteDynamid(Long dynamicId);


    /**
     *  热搜动态查找
     */
    ZheXingResult hotDynamic();

    /**
     *  推荐动态查找
     */


    /**
     *  查看已关注人的动态
     */



    /**
     *  查看某人发的动态
     */

    /**
     *  动态点赞
     */

    /**
     *  动态转发（分享）
     */

    /**
     *  动态收藏
     */

    /**
     *  动态举报
     */

}
