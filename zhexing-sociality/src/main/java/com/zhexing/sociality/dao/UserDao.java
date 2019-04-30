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
     * @param dynamic
     * @return
     */
    @Select("SELECT uname,unickname,uchathead FROM tb_user WHERE user_id = #{dynamic.userId}")
    List<DynUser> selectById(@Param(value = "dynamic") Dynamic dynamic);


}
