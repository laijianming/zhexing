package com.zhexing.social.dao;

import com.zhexing.common.pojo.Tag;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface TagDao {

    /**
     *  批量插入tag话题
     * @param tags
     */
    @Insert({
            "<script>",
            "insert into tb_tag(tname, tcreater, tstatus,create_time) values ",
            "<foreach collection='tags' item='item' index='index' separator=','>",
            "(#{item.tname}, #{item.tcreater}, #{item.tstatus}, #{item.createTime})",
            "</foreach>",
            "</script>"
    })
    @Options(useGeneratedKeys=true, keyProperty="item.tagId", keyColumn="tag_id")
    void createTags(@Param(value = "tags")ArrayList<Tag> tags);


    /**
     *  查找数据库是否含有此话题
     * @param tname
     * @return
     */
    @Select("SELECT tag_id,tname,tcreater,tstatus,create_time FROM tb_tag WHERE tname = #{tname}")
    Tag search(String tname);
}
