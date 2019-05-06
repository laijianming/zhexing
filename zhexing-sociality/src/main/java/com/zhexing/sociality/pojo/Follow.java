package com.zhexing.sociality.pojo;

import lombok.Data;

@Data
public class Follow {

    private Long followId;

    private Long userId;

    private Long followedUser;

    private String uname;

    private String unickname;

    private String uchathead;


}
