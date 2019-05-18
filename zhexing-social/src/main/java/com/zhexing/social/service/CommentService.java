package com.zhexing.social.service;

import com.zhexing.common.pojo.Comment;
import com.zhexing.common.resultPojo.ZheXingResult;

/**
 *  评论业务处理接口
 */
public interface CommentService {


    /**
     *  动态评论
     */
    ZheXingResult dynamicComment(Comment comment, String tname);


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
    ZheXingResult likeComment(Long dynamicId,Long commentId,Long userId,String tname,boolean flag);



    /**
     *  查找前几条的热评
     */
    ZheXingResult searchHotComment(Long userId,Long dynamicId,int start,int n);


    /**
     *  查找该动态的全部评论
     */
    ZheXingResult getDynamicComments(Long dynamicId,int start,int n,Long userId);

    /**
     *  评论评论
     */
    ZheXingResult commentComment(Comment comment);

    /**
     *  评论评论删除
     */
    ZheXingResult deleteCComment(Long commentId,Long parentId);


    /**
     * 查看评论中的所有评论
     */
    ZheXingResult getCComments(Long parentId,Long userId);


}
