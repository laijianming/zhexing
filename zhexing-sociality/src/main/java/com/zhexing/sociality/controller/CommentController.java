package com.zhexing.sociality.controller;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.pojo.Comment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {


    @PostMapping("/comment/dynamic")
    public ZheXingResult dynamicComment(Comment comment){

        return null;
    }


}
