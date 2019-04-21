package com.zhexing.sociality.service;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.pojo.Comment;

/**
 *  评论业务处理接口
 */
public interface CommentService {


    /**
     *  动态评论
     */
    ZheXingResult dynamicComment(Comment comment,String tname);


    /**
     *  动态评论删除
     */
    ZheXingResult deleteComment(Long commentId,Long dynamicId,String tname);

    /**
     * 评论点赞
     * @param commentId
     * @param userId
     * @param tname
     * @param flag 1 表示 点赞； 0（其他）表示 取消点赞
     * @return
     */
    ZheXingResult likeComment(Long commentId,Long userId,String tname,int flag);



    /**
     *  查找前几条的热评
     */
    ZheXingResult searchHotComment(Long dynamicId,int start,int n);

    /**
     *  查找该动态的全部评论
     */

    /**
     *  评论评论
     */

    /**
     *  评论评论删除
     */

    /**
     *  评论评论点赞处理
     */

    /**
     * 查看评论中的所有评论
     */


}
