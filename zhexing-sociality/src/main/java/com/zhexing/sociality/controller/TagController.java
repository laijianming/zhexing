package com.zhexing.sociality.controller;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.service.TagService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 话题处理controller
 */
@RestController
@Api(description = "话题处理")
public class TagController {

    @Autowired
    TagService tagService;

    /**
     * 获取热搜话题
     * @return
     */
    @GetMapping("/tag/hotTag")
    public ZheXingResult hotTag(){
        return tagService.hotTags();
    }

    /**
     * 查找话题
     * @param tname
     * @return
     */
    @GetMapping("/tag/searchTag")
    public ZheXingResult searchTag(String tname){
        return ZheXingResult.ok(tagService.searchTag(tname));
    }


}
