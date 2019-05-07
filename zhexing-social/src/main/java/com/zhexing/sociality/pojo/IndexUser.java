package com.zhexing.sociality.pojo;

import lombok.Data;

@Data
public class IndexUser {

    private Long userId;

    private String uname;

    private String unickname;

    private String uchathead;

    private Long dynamicCount;

    private Long followCount;

    private Long followerCount;

    // 自己是否关注对方
    private Long following;

    // 对方是否关注自己
    private Long follower;

    // 是否关注 0 未互关；1 关注对方；2 被对方关注；3 互关
//    private Long action;


}
