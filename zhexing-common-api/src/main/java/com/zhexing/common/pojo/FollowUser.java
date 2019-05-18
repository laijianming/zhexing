package com.zhexing.common.pojo;

import lombok.Data;

@Data
public class FollowUser {


    private Long userId;

    private String uname;

    private String unickname;

    private String uchathead;

    private Long followCount;

    private Long followerCount;

    private Long dynamicCount;

    // 自己是否关注对方
    private Long following;

    // 对方是否关注自己
    private Long follower;





}
