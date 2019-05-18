package com.zhexing.common.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {


    private Long commentId;

    private Long dynamicId;

    private Long userId;

    private String unickname;

    private String uchathead;

    private String uname;

    private String content;

    private Date commentTime;


    private boolean action;

    private Long likesCount;

    private Long commentsCount;

    // 父评论id
    private Long parentId;



}
