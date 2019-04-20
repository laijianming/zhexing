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
     * @param tnames 该动态包含的话题
     * @return
     */
    ZheXingResult deleteDynamid(Long dynamicId,String tnames);


    /**
     * 热搜话题下动态查找
     * @param tname 话题名
     * @param start 开始的条数
     * @param nums 查出多少条
     * @return
     */
    ZheXingResult hotDynamic(String tname,int start,int nums);


    /**
     *  推荐动态查找
     */
    ZheXingResult recommend(Long start,Long end);


    /**
     *  查看已关注人的动态
     */



    /**
     *  查看某人发的动态
     */


    /**
     * 动态点赞处理
     * @param userId
     * @param dynamicId
     * @param tnames
     * @param flag 1 表示 点赞； 0（其他）表示取消点赞 -1
     * @return
     */
    ZheXingResult likeDynamic(Long userId,Long dynamicId,String tnames,int flag);



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
