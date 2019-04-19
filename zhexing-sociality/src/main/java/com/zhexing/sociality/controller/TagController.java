package com.zhexing.sociality.controller;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController {

    @Autowired
    TagService tagService;

    @GetMapping("/tag/hotTag")
    public ZheXingResult hotTag(){
        return tagService.hotTags();
    }


}
