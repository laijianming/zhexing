package com.zhexing.sociality.dao;


import com.zhexing.sociality.pojo.Dynamic;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 *  动态dao层
 */
@Mapper
public interface DynamicDao {

    /**
     *  测试查找所有动态
     * @return
     */
    @Select("select * from tb_dynamic")
    List<Dynamic> getDynamic();

    /**
     *  发布动态并返回自增主键
     * @param dynamic
     * @return
     */
    @Insert("INSERT INTO tb_dynamic(user_id,publish_time,content,tnames,images,forward_did)" +
            "VALUES (#{dynamic.userId},#{dynamic.publishTime},#{dynamic.content},#{dynamic.tnames}," +
            "#{dynamic.images},#{dynamic.forwardDid})")
    @Options(useGeneratedKeys=true, keyProperty="dynamic.dynamicId", keyColumn="dynamic_id") // 注：keyProperty要填 "对象. 属性" 自增主键才能被封装进对象中
    Long publishDynamic(@Param(value = "dynamic") Dynamic dynamic);


    @Delete("DELETE FROM tb_dynamic WHERE dynamic_id = #{dynamicId}")
    Integer deleteDynamic(Long dynamicId);

}
