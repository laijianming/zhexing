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
        System.out.println("dynamic == " + dynamic);
        if(dynamic == null)
            return ZheXingResult.ok();
        return dynamicService.publishDynamic(dynamic);
    }


    /**
     *  删除动态
     * @param dynamicId
     * @return
     */
    @GetMapping("/dynamic/delete")
    public ZheXingResult deleteDynamic(Long dynamicId,String tnames){
        return dynamicService.deleteDynamid(dynamicId,tnames);
    }

    /**
     * 获取热点话题下的动态
     * @param tname
     * @param start
     * @param nums
     * @return
     */
    @GetMapping("/dynamic/hotDynamic")
    public ZheXingResult searchHotDynamic(String tname,int start,int nums){
        return dynamicService.hotDynamic(tname,start,nums);
    }





}
