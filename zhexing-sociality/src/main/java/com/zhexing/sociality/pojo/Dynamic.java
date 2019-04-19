package com.zhexing.sociality.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Dynamic implements Serializable {

    private Long dynamicId;

    private Long userId;

    private Date publishTime;

    private String content;
    // 话题id
    private String tnames;

    private String images;

    private Long forwardDid;

    // 是否点赞 1：点赞 0：没点赞
    private Long isLike;

    // 评论数
    private Long comments;

    // 转发数
    private Long follows;


}
