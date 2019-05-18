package com.zhexing.social.controller;


import com.zhexing.common.pojo.Dynamic;
import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.social.service.DynamicService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ZheXingResult searchHotDynamic(String tname,int start,int nums,Long userId){
        return dynamicService.hotDynamic(tname,start,nums,userId);
    }


    /**
     * 推荐动态查找
     * @param start 起始位置
     * @param end 结束位置
     * @return
     */
    @GetMapping("/dynamic/recommend")
    public ZheXingResult recommendDynamic(Long start,Long end,Long userId){
        return dynamicService.recommend(start,end,userId);
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
    public ZheXingResult likeDynamic(Long userId,Long dynamicId,String tnames,boolean flag){
        return dynamicService.likeDynamic(userId, dynamicId, tnames, flag);
    }


    /**
     * 查看已关注人的动态
     * @param userId 当前用户id
     * @param start 开始的条数
     * @param end 查多少条
     * @return
     */
    @GetMapping("/dynamic/allFollowDynamic")
    ZheXingResult allFollowDynamics(Long userId,Long start,Long end){
        return dynamicService.allFollowDynamics(userId, start, end);
    }

    /**
     * 查看某人发的动态
     * @param uname 关注的人的uname
     * @param start 开始的条数
     * @param end 查多少条
     * @return
     */
    @GetMapping("/dynamic/followDynamic")
    ZheXingResult followDynamic(Long userId,String uname,Long start,Long end){
        return dynamicService.followDynamic(userId, uname, start, end);
    }


    // TODO 下面的方法未实现


    /**
     * 动态转发（分享）
     * @param dynamic
     * @return
     */
    ZheXingResult forwardDynamic(Dynamic dynamic){
        return dynamicService.forwardDynamic(dynamic);
    }

    /**
     *  动态收藏
     * @param dynamicId
     * @return
     */
    ZheXingResult collectDynamic(Long dynamicId){
        return dynamicService.collectDynamic(dynamicId);
    }


    /**
     *  动态举报
     */


}
