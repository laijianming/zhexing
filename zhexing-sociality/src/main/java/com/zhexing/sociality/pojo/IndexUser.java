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

    // 是否关注
    private Long action;


}
