package com.zhexing.sociality.dao;

import com.zhexing.sociality.pojo.Comment;
import org.apache.ibatis.annotations.*;

/**
 *  评论dao层
 */
@Mapper
public interface CommentDao {


    /**
     *  发布评论并返回自增主键
     * @param comment
     * @return
     */
    @Insert("INSERT INTO tb_comment(comment_id,dynamic_id,user_id,unickname,uname,content,comment_time,parent_id)" +
            "VALUES (#{comment.comment_id},#{comment.dynamic_id},#{comment.user_id},#{comment.unickname}," +
            "#{comment.uname},#{comment.content},#{comment.comment_time},#{comment.parent_id})")
    @Options(useGeneratedKeys=true, keyProperty="comment.commentId", keyColumn="comment_id") // 注：keyProperty要填 "对象. 属性" 自增主键才能被封装进对象中
    Long publishComment(@Param(value = "comment") Comment comment);


    /**
     * 删除评论
     * @param commentId
     * @return
     */
    @Delete("DELETE FROM tb_comment WHERE comment_id = #{commentId}")
    Long deleteComment(Long commentId);

}
