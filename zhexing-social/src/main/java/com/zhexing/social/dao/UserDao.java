package com.zhexing.social.dao;


import com.zhexing.common.pojo.DynUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDao {

    /**
     * 根据 userId 来查找动态用户详细信息
     * @param userId
     * @return
     */
    @Select("SELECT uname,unickname,uchathead FROM tb_user WHERE user_id = #{userId}")
    List<DynUser> selectById(@Param(value = "userId") Long userId );


    /**
     * 根据userId来查找关注的用户的id
     * @param userId
     * @return
     */
    @Select("SELECT followed_user FROM tb_follow WHERE user_id = #{userId}")
    List<Long> selectFollowUserByUserId(Long userId);


    /**
     *  根据uname查找userId
     * @param uname
     * @return
     */
    @Select("SELECT user_id FROM tb_user WHERE uname = #{uname}")
    Long selectUserIdByUname(String uname);

    /**
     * 模糊查找用户
     * @param uname
     * @param unickname
     * @param start
     * @param end
     * @return
     */
    @Select("SELECT user_id FROM tb_user WHERE uname LIKE #{uname} OR unickname LIKE #{unickname} LIMIT #{start},#{end}")
    List<Long> selectUsersByUserIdOrUname(@Param("uname") String uname, @Param("unickname") String unickname, @Param("start") Long start, @Param("end") Long end);

}
