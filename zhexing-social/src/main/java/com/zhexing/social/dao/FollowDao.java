package com.zhexing.social.dao;

import com.zhexing.social.pojo.Follow;
import com.zhexing.social.pojo.FollowUser;
import com.zhexing.social.pojo.IndexUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FollowDao {

    /**
     * 给tb_follow插入一条记录
     * @param follow
     * @return
     */
    @Insert("INSERT INTO tb_follow (user_id,followed_user) VALUES (#{follow.userId},#{follow.follower})")
    Long followUser(@Param("follow") Follow follow);

    /**
     * 查找是否已关注
     * @param follow
     * @return
     */
    @Select("SELECT follow_id FROM tb_follow WHERE user_id = #{follow.userId}, followed_user = #{follow.follower}")
    Long checkFollow(@Param("follow") Follow follow);


    /**
     * 给tb_follow删除一条记录 根据user_id followed_user来删除
     * @param follow
     * @return
     */
    @Delete("DELETE FROM tb_follow WHERE user_id = #{follow.userId} AND followed_user = #{follow.follower}")
    Long cancelFollow(@Param("follow") Follow follow);

    /**
     * 查找所有关注的人 并补全其他信息
     * @param userId
     * @return
     */
    @Select("SELECT user_id FROM tb_user WHERE user_id IN " +
            "(SELECT followed_user FROM tb_follow WHERE user_id = #{userId}) ")
    List<FollowUser> searchAllFollow(Long userId);


    /**
     * 查找所有被关注的人 并补全其他信息
     * @param followerUser
     * @return
     */
    @Select("SELECT t1.user_id FROM tb_user t1 " +
            "WHERE t1.user_id IN (SELECT user_id FROM tb_follow WHERE followed_user = #{followerUser}) ")
    List<FollowUser> searchAllFollowedUser(Long followerUser);




    /**
     * 查看个人主页，需 user_id,uname,unickname,uchathead,followerCount,followCount,action,dynamicCount
     * @param indexId
     * @param userId
     * @return
     */
    //SELECT COUNT(user_id) AS followedCount,b.followCount FROM tb_follow,(SELECT COUNT(followed_user) AS followCount FROM tb_follow WHERE user_id = 2 ) b WHERE followed_user = 2 ;
//    @Select("SELECT a.followerCount,b.followCount,c.user_id,c.uname,c.unickname,c.uchathead,d.action,e.dynamicCount " +
//            "FROM (SELECT COUNT(user_id) AS followerCount FROM tb_follow  WHERE followed_user = (SELECT user_id FROM tb_user WHERE uname = #{uname})) AS a , " +
//            "(SELECT COUNT(followed_user) AS followCount FROM tb_follow  WHERE user_id = (SELECT user_id FROM tb_user WHERE uname = #{uname})) AS b," +
//            "tb_user AS c , (SELECT follow_id AS action FROM tb_follow WHERE user_id = #{userId} AND followed_user = (SELECT user_id FROM tb_user WHERE uname = #{uname})) AS d, " +
//            "(SELECT COUNT(dynamic_id) AS dynamicCount FROM tb_dynamic WHERE user_id = (SELECT user_id FROM tb_user WHERE uname = #{uname})) AS e " +
//            "WHERE c.user_id = (SELECT user_id FROM tb_user WHERE uname = #{uname})")

    @Select("SELECT t1.user_id,t1.uname,t1.unickname,t1.uchathead,t2.followerCount,t3.followCount,t4.dynamicCount,t5.following,t6.follower FROM tb_user t1  " +
            "JOIN (SELECT COUNT(user_id) AS followerCount FROM tb_follow WHERE followed_user = #{indexId}) t2 " +
            "JOIN (SELECT COUNT(followed_user) AS followCount FROM tb_follow WHERE user_id = #{indexId}) t3 " +
            "JOIN (SELECT COUNT(dynamic_id) AS dynamicCount FROM tb_dynamic WHERE user_id = #{indexId}) t4 " +
            "JOIN (SELECT MIN(follow_id) AS following FROM tb_follow WHERE EXISTS (SELECT follow_id FROM tb_follow WHERE user_id = #{userId} AND followed_user = #{indexId})) t5 " +
            "JOIN (SELECT MIN(follow_id) AS follower FROM tb_follow WHERE EXISTS (SELECT follow_id FROM tb_follow WHERE followed_user = #{userId} AND user_id = #{indexId})) t6 " +
            "WHERE t1.user_id = #{indexId};")
    IndexUser selectUserFollowInfo(@Param("indexId") Long indexId, @Param("userId") Long userId);



}
