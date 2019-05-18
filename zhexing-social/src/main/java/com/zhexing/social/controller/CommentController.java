package com.zhexing.social.controller;

import com.zhexing.common.pojo.Comment;
import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.social.service.CommentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 评论处理Controller
 */
@RestController
@Api(description = "评论处理")
public class CommentController {

    @Autowired
    CommentService commentService;

    /**
     * 动态评论
     * @param comment 评论
     * @param tnames 该动态包含的话题
     * @return
     */
    @PostMapping("/comment/dynamic")
    public ZheXingResult dynamicComment(Comment comment, String tnames){
        return commentService.dynamicComment(comment,tnames);
    }

    /**
     * 评论删除
     * @param commentId
     * @param dynamicId
     * @param tnames
     * @return
     */
    @DeleteMapping("/comment/dynamic")
    public ZheXingResult deleteComment(Long commentId,Long dynamicId,String tnames){
        return commentService.deleteComment(commentId, dynamicId, tnames);
    }

    /**
     * 查找前几条的热评
     * @param dynamicId
     * @param start
     * @param n
     * @return
     */
    @GetMapping("/comment/hotComment")
    ZheXingResult searchHotComment(Long userId,Long dynamicId,int start,int n){
        return commentService.searchHotComment(userId,dynamicId, start, n);
    }

    /**
     * 评论点赞处理
     * @param commentId
     * @param userId
     * @param tname
     * @param flag 1 表示 点赞； 0（其他）表示 取消点赞
     * @return
     */
    @GetMapping("/comment/likeComment")
    ZheXingResult likeComment(Long dynamicId,Long commentId,Long userId,String tname,boolean flag){
        return commentService.likeComment(dynamicId,commentId, userId, tname, flag);
    }

    /**
     * 查找该动态的评论
     * @param dynamicId
     * @param start 从第几条查
     * @param n 查几条
     * @param userId
     * @return
     */
    @GetMapping("/comment/dynamicComment")
    ZheXingResult getDynamicComments(Long dynamicId,int start,int n,Long userId){
        return commentService.getDynamicComments(dynamicId, start, n, userId);
    }

    /**
     * 评论评论
     * @param comment
     * @return
     */
    @PostMapping("/comment/comment")
    ZheXingResult commentComment(Comment comment){
        return commentService.commentComment(comment);
    }

    /**
     * 评论评论删除
     * @param commentId
     * @param parentId
     * @return
     */
    @DeleteMapping("/comment/comment")
    ZheXingResult deleteCComment(Long commentId,Long parentId){
        return commentService.deleteCComment(commentId, parentId);
    }

    /**
     * 查看评论中的所有评论
     * @param parentId
     * @return
     */
    @GetMapping("/comment/comment")
    ZheXingResult getCComments(Long parentId,Long userId){
        return commentService.getCComments(parentId,userId);
    }




}
