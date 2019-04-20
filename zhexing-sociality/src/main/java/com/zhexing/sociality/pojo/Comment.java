package com.zhexing.sociality.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {


    private Long commentId;

    private Long dynamicId;

    private Long userId;

    private String unickname;

    private String uname;

    private String content;

    // 父评论id
    private Long parentId;



}
