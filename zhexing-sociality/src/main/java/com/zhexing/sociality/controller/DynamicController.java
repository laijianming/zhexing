package com.zhexing.sociality.controller;


import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.pojo.Dynamic;
import com.zhexing.sociality.service.DynamicService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  动态接口
 */
@RestController
@Api(description = "动态处理")
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


    /**
     * 推荐动态查找
     * @param start 起始位置
     * @param end 结束位置
     * @return
     */
    @GetMapping("/dynamic/recommend")
    public ZheXingResult recommendDynamic(Long start,Long end){
        return dynamicService.recommend(start,end);
    }

    /**
     * 动态点赞处理
     * @param userId
     * @param dynamicId
     * @param tnames 包含的话题
     * @param flag 1 表示 点赞； 0（其他）表示取消点赞
     * @return
     */
    @GetMapping("/dynamic/likeDynamic")
    public ZheXingResult likeDynamic(Long userId,Long dynamicId,String tnames,int flag){
        return dynamicService.likeDynamic(userId, dynamicId, tnames, flag);
    }



}
