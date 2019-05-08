package com.zhexing.social.dao;


import com.zhexing.social.pojo.Dynamic;
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
    @Insert("INSERT INTO tb_dynamic(user_id,publish_time,content,tnames,images,mimages,forward_did)" +
            "VALUES (#{dynamic.userId},#{dynamic.publishTime},#{dynamic.content},#{dynamic.tnames}," +
            "#{dynamic.images},#{dynamic.mimages},#{dynamic.forwardDid})")
    @Options(useGeneratedKeys=true, keyProperty="dynamic.dynamicId", keyColumn="dynamic_id") // 注：keyProperty要填 "对象. 属性" 自增主键才能被封装进对象中
    Long publishDynamic(@Param(value = "dynamic") Dynamic dynamic);


    /**
     * 根据 dynamicId 来查找动态详细信息
     * @param dynamicId
     * @return
     */
    @Select("SELECT dynamic_id,user_id,publish_time,content,tnames,images,mimages,forward_did FROM tb_dynamic WHERE dynamic_id = #{dynamicId}")
    Dynamic selectById(Long dynamicId);

    /**
     * 根据 dynamicId 来删除数据库中的对应的动态
     * @param dynamicId
     * @return
     */
    @Delete("DELETE FROM tb_dynamic WHERE dynamic_id = #{dynamicId}")
    Integer deleteDynamic(Long dynamicId);


    /**
     * 根据userId查找dynamicId
     * @param uname
     * @return
     */
    @Select("SELECT dynamic_id FROM tb_dynamic WHERE user_id = (SELECT user_id FROM tb_user WHERE uname = #{uname}) ORDER BY publish_time DESC LIMIT #{start},#{end}")
    List<Long> selectDynamicIdByUserId(@Param(value = "uname")String uname,@Param(value = "start")Long start,@Param(value = "end")Long end);

    /**
     * 根据dynamicIds 来批量查询dynamic
     * @param dynamicIds
     * @return
     */
    @Select({
            "<script>" +
            "SELECT dynamic_id,user_id,publish_time,content,tnames,images,mimages,forward_did FROM tb_dynamic WHERE dynamic_id IN (" +
            "<foreach collection='dynamicIds' item='item' index='index' separator=','>" +
            "#{item}" +
            "</foreach>" +
            ")  ORDER BY publish_time DESC " +
            "</script>"
            })
    List<Dynamic> selectDynamicByIds(@Param(value = "dynamicIds")List dynamicIds);


    /**
     * 根据userIds来查找end条dynamic记录并按时间逆序排序
     * @param userIds
     * @param start
     * @param end
     * @return
     */
    //SELECT * FROM tb_dynamic WHERE user_id IN (99,88,71,2) ORDER BY publish_time DESC LIMIT 4,2
    @Select({
            "<script>",
            "SELECT dynamic_id,user_id,publish_time,content,tnames,images,mimages,forward_did FROM tb_dynamic WHERE user_id IN (",
            "<foreach collection='userIds' item='item' index='index' separator=','>",
            "#{item}",
            "</foreach>",
            ") ORDER BY publish_time DESC LIMIT #{start},#{end}",
            "</script>"
    })
    // 需用@Param(value = "") 指定参数，否则报错 Resolved [org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.binding.BindingException: Parameter 'start' not found. Available parameters are [arg2, arg1, userIds, param3, param1, param2]]
    List<Dynamic> selectDynamicByUserIds(@Param(value = "userIds")List<Long> userIds,@Param(value = "start")Long start,@Param(value = "end")Long end);


}
