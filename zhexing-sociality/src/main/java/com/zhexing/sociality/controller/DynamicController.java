package com.zhexing.sociality.controller;


import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.pojo.Dynamic;
import com.zhexing.sociality.service.DynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  动态接口
 */
@RestController
public class DynamicController {

    @Autowired
    DynamicService dynamicService;

    /**
     *  发布动态
     * @param dynamic
     * @return
     */
    @PostMapping("/dynamic/publish")
    public ZheXingResult publishDynamic(Dynamic dynamic){

        return ZheXingResult.ok(dynamicService.publishDynamic(dynamic));
    }


    /**
     *  删除动态
     * @param dynamicId
     * @return
     */
    @GetMapping("/dynamic/delete")
    public ZheXingResult deleteDynamic(Long dynamicId){
        return dynamicService.deleteDynamid(dynamicId);
    }

    @GetMapping("/dynamic/hotDynamic")
    public ZheXingResult searchHotDynamic(){
        return dynamicService.hotDynamic();
    }


}
