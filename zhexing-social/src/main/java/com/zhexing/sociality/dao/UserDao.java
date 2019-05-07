package com.zhexing.sociality.dao;


import com.zhexing.sociality.pojo.DynUser;
import com.zhexing.sociality.pojo.Dynamic;
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


    @Select("SELECT user_id FROM tb_user WHERE uname = #{uname}")
    Long selectUserIdByUname(String uname);


}
