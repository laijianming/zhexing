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

}
