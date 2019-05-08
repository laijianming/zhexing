package com.zhexing.social.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Dynamic implements Serializable {

    private Long dynamicId;

    private Long userId;

    private String uname;

    private String unickname;

    private String uchathead;

    private Date publishTime;

    private String content;

    // 话题id
    private String tnames;

    private String images;

    // 缩略图
    private String mimages;

    private List<Image> imgList;

    private Long forwardDid;

    // 是否点赞
    private boolean action;

    // 点赞数
    private long likesCount;

    // 评论数
    private long commentsCount;

    // 转发数
    private long followsCount;


}
