package com.zhexing.sociality.controller;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.pojo.Comment;
import com.zhexing.sociality.service.CommentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ZheXingResult dynamicComment(Comment comment,String tnames){
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
    @GetMapping("/comment/dynamic")
    ZheXingResult searchHotComment(Long dynamicId,int start,int n){
        return commentService.searchHotComment(dynamicId, start, n);
    }

    /**
     * 评论点赞处理
     * @param commentId
     * @param userId
     * @param tname
     * @param flag 1 表示 点赞； 0（其他）表示 取消点赞
     * @return
     */
    @GetMapping("/comment/likeDynamic")
    ZheXingResult likeComment(Long commentId,Long userId,String tname,int flag){
        return commentService.likeComment(commentId, userId, tname, flag);
    }


}
