package com.zhexing.sociality.dao;

import com.zhexing.sociality.pojo.Follow;
import com.zhexing.sociality.pojo.FollowUser;
import com.zhexing.sociality.pojo.IndexUser;
import org.apache.ibatis.annotations.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Mapper
public interface FollowDao {

    /**
     * 给tb_follow插入一条记录
     * @param follow
     * @return
     */
    @Insert("INSERT INTO tb_follow user_id,followed_user VALUES (#{follow.userId},#{follow.followedUser})")
    Long followUser(Follow follow);


    /**
     * 给tb_follow删除一条记录 根据user_id followed_user来删除
     * @param follow
     * @return
     */
    @Delete("DELETE FROM tb_follow WHERE user_id = #{follow.userId} AND followed_user = #{follow.followedUser}")
    Long cancelFollow(Follow follow);

    /**
     * 查找所有关注的人 并补全其他信息
     * @param userId
     * @return
     */
    @Select("SELECT user_id,uname,unickname,uchathead FROM tb_user WHERE user_id IN " +
            "(SELECT followed_user FROM tb_follow WHERE user_id = #{userId}) ")
    List<FollowUser> searchAllFollow(Long userId);


    /**
     * 查找所有被关注的人 并补全其他信息
     * @param followerUser
     * @return
     */
    @Select("SELECT user_id,uname,unickname,uchathead FROM tb_user WHERE user_id IN " +
            "(SELECT user_id FROM tb_follow WHERE followed_user = #{followerUser}) ")
    List<FollowUser> searchAllFollowedUser(Long followerUser);

    /**
     * 查看个人主页，需 user_id,uname,unickname,uchathead,followerCount,followCount,action,dynamicCount
     * @param uname
     * @param userId
     * @return
     */
    //SELECT COUNT(user_id) AS followedCount,b.followCount FROM tb_follow,(SELECT COUNT(followed_user) AS followCount FROM tb_follow WHERE user_id = 2 ) b WHERE followed_user = 2 ;
    @Select("SELECT COUNT(a.user_id) AS followerCount,b.followCount,c.user_id,c.uname,c.unickname,c.uchathead,d.action,e.dynamicCount " +
            "FROM tb_follow AS a , (SELECT COUNT(followed_user) AS followCount FROM tb_follow  WHERE user_id = (SELECT user_id FROM tb_user WHERE uname = #{uname})) AS b," +
            "tb_user AS c , (SELECT follow_id AS action FROM tb_follow WHERE user_id = #{userId} AND followed_user = (SELECT user_id FROM tb_user WHERE uname = #{uname})) AS d, " +
            "(SELECT COUNT(dynamic_id) AS dynamicCount FROM tb_dynamic WHERE user_id = (SELECT user_id FROM tb_user WHERE uname = #{uname})) AS e " +
            "WHERE c.user_id = (SELECT user_id FROM tb_user WHERE uname = #{uname})")
    IndexUser selectUserFollowInfo(@Param("uname") String uname,@Param("userId") Long userId);



}
