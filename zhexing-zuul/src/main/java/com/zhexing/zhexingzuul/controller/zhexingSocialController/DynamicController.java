package com.zhexing.zhexingzuul.controller.zhexingSocialController;


import com.zhexing.common.pojo.Dynamic;
import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.zhexingzuul.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *  动态接口
 */
@RestController
public class DynamicController {

    @Autowired
    SocialService socialService;
    
    /**
     *  发布动态
     * @param dynamic
     * @return
     */
    @PostMapping("/dynamic/publish")
    public ZheXingResult publishDynamic(Dynamic dynamic){
        return socialService.publishDynamic(dynamic);
    }


    /**
     *  删除动态
     * @param dynamicId
     * @return
     */
    @GetMapping("/dynamic/delete")
    public ZheXingResult deleteDynamic(Long dynamicId,String tnames){
        return socialService.deleteDynamic(dynamicId,tnames);
    }

    /**
     * 获取热点话题下的动态
     * @param tname
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/dynamic/hotDynamic")
    public ZheXingResult searchHotDynamic(@RequestParam("tname") String tname,@RequestParam("start") Integer start,
                                          @RequestParam("end")Integer end,@RequestParam("userId") Long userId){
        System.out.println("tname == " + tname);
        return socialService.searchHotDynamic(tname,start,end,userId);
    }


    /**
     * 推荐动态查找
     * @param start 起始位置
     * @param end 结束位置
     * @return
     */
    @GetMapping("/dynamic/recommend")
    public ZheXingResult recommendDynamic(Long start,Long end,Long userId){
        System.out.println(start + " "+ end + "" + userId);
        return socialService.recommendDynamic(start,end,userId);
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
        System.out.println("likeDynamic " + userId + " " + dynamicId + " " + tnames + " " + flag);
        return socialService.likeDynamic(userId, dynamicId, tnames, flag);
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
        return socialService.allFollowDynamics(userId, start, end);
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
        return socialService.followDynamic(userId, uname, start, end);
    }


    // TODO 下面的方法未实现


    /**
     * 动态转发（分享）
     * @param dynamic
     * @return
     */
    @PostMapping("/dynamic/forwardDynamic")
    ZheXingResult forwardDynamic(Dynamic dynamic){
        return socialService.forwardDynamic(dynamic);
    }

    /**
     *  动态收藏
     * @param dynamicId
     * @return
     */
    @GetMapping("/dynamic/collectDynamic")
    ZheXingResult collectDynamic(Long dynamicId){
        return socialService.collectDynamic(dynamicId);
    }


    /**
     *  动态举报
     */


}
