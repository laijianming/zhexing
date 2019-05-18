package com.zhexing.zhexingzuul.service;

import com.zhexing.common.pojo.Comment;
import com.zhexing.common.pojo.Dynamic;
import com.zhexing.common.pojo.Follow;
import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.zhexingzuul.fallback.SocialFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(value = "ZHEXING-SOCIAL",fallbackFactory = SocialFallback.class)
@Service
public interface SocialService {


    /***            CommentController              ***/

    /**
     * 动态评论
     * @param comment 评论
     * @param tnames 该动态包含的话题
     * @return
     */
//    @PostMapping("/comment/dynamic")
    @RequestMapping(value = "/comment/dynamic",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    ZheXingResult dynamicComment(Comment comment, @RequestParam("tnames") String tnames);



    /**
     * 评论删除
     * @param commentId
     * @param dynamicId
     * @param tnames
     * @return
     */
    @DeleteMapping("/comment/dynamic")
    ZheXingResult deleteComment(@RequestParam("commentId") Long commentId,@RequestParam("dynamicId") Long dynamicId,@RequestParam("tnames") String tnames);


    /**
     * 查找前几条的热评
     * @param dynamicId
     * @param start
     * @param n
     * @return
     */
    @GetMapping("/comment/hotComment")
    ZheXingResult searchHotComment(@RequestParam("userId") Long userId,@RequestParam("dynamicId") Long dynamicId,@RequestParam("start") int start,@RequestParam("n") int n);


    /**
     * 评论点赞处理
     * @param commentId
     * @param userId
     * @param tname
     * @param flag 1 表示 点赞； 0（其他）表示 取消点赞
     * @return
     */
    @GetMapping("/comment/likeComment")
    ZheXingResult likeComment(@RequestParam("dynamicId") Long dynamicId,@RequestParam("commentId") Long commentId,
                              @RequestParam("userId") Long userId, @RequestParam("tname") String tname,
                              @RequestParam("flag") boolean flag);


    /**
     * 查找该动态的评论
     * @param dynamicId
     * @param start 从第几条查
     * @param n 查几条
     * @param userId
     * @return
     */
    @GetMapping("/comment/dynamicComment")
    ZheXingResult getDynamicComments(@RequestParam("dynamicId") Long dynamicId,@RequestParam("start") int start,
                                     @RequestParam("n") int n,@RequestParam("userId") Long userId);

    /**
     * 评论评论
     * @param comment
     * @return
     */
    @PostMapping(value = "/comment/comment",consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    ZheXingResult commentComment(Comment comment);

    /**
     * 评论评论删除
     * @param commentId
     * @param parentId
     * @return
     */
    @DeleteMapping("/comment/comment")
    ZheXingResult deleteCComment(@RequestParam("commentId") Long commentId,@RequestParam("parentId") Long parentId);

    /**
     * 查看评论中的所有评论
     * @param parentId
     * @return
     */
    @GetMapping("/comment/comment")
    ZheXingResult getCComments(@RequestParam("parentId") Long parentId,@RequestParam("userId") Long userId);




    /***            DynamicController              ***/

    /**
     *  发布动态
     * @param dynamic
     * @return
     */
    @PostMapping(value = "/dynamic/publish",consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    ZheXingResult publishDynamic(Dynamic dynamic);

    /**
     *  删除动态
     * @param dynamicId
     * @return
     */
    @GetMapping("/dynamic/delete")
    ZheXingResult deleteDynamic(@RequestParam("dynamicId") Long dynamicId,@RequestParam("tnames") String tnames);


    /**
     * 获取热点话题下的动态
     * @param tname
     * @param start
     * @param nums
     * @return
     */
    @GetMapping("/dynamic/hotDynamic")
    ZheXingResult searchHotDynamic(@RequestParam("tname") String tname,@RequestParam("start") int start,@RequestParam("nums") int nums,@RequestParam("userId") Long userId);

    /**
     * 推荐动态查找
     * @param start 起始位置
     * @param end 结束位置
     * @return
     */
    @GetMapping("/dynamic/recommend")
    ZheXingResult recommendDynamic(@RequestParam("start") Long start,@RequestParam("end") Long end,@RequestParam("userId") Long userId);


    /**
     * 动态点赞处理
     * @param userId
     * @param dynamicId
     * @param tnames 包含的话题
     * @param flag 1 表示 点赞； 0（其他）表示取消点赞
     * @return
     */
    @GetMapping("/dynamic/likeDynamic")
    ZheXingResult likeDynamic(@RequestParam("userId") Long userId,@RequestParam("dynamicId") Long dynamicId,@RequestParam("tnames") String tnames,@RequestParam("flag") boolean flag);

    /**
     * 查看已关注人的动态
     * @param userId 当前用户id
     * @param start 开始的条数
     * @param end 查多少条
     * @return
     */
    @GetMapping("/dynamic/allFollowDynamic")
    ZheXingResult allFollowDynamics(@RequestParam("userId") Long userId,@RequestParam("start") Long start,@RequestParam("end") Long end);

    /**
     * 查看某人发的动态
     * @param uname 关注的人的uname
     * @param start 开始的条数
     * @param end 查多少条
     * @return
     */
    @GetMapping("/dynamic/followDynamic")
    ZheXingResult followDynamic(@RequestParam("userId") Long userId,@RequestParam("uname") String uname,@RequestParam("start") Long start,@RequestParam("end") Long end);

    /**
     * 动态转发（分享）
     * @param dynamic
     * @return
     */
    @PostMapping(value = "/dynamic/forwardDynamic",consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    ZheXingResult forwardDynamic(Dynamic dynamic);

    /**
     *  动态收藏
     * @param dynamicId
     * @return
     */
    @GetMapping("/dynamic/collectDynamic")
    ZheXingResult collectDynamic(@RequestParam("dynamicId") Long dynamicId);



    /***            FileController              ***/


    /**
     * 上传图片
     * @param uploadFile
     * @return
     */
//    @PostMapping("/social/upload/file")
    @RequestMapping(value = "/social/upload/file",method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ZheXingResult uploadFile(@RequestBody MultipartFile uploadFile);

    /**
     * 上传图片
     * @param image 图片的base64编码字符串
     * @return
     */
    @PostMapping("/social/upload/image64")
    ZheXingResult uploadImage64(@RequestParam("image") String image);



    /***            FollowController              ***/


    /**
     * 关注处理
     * @param follow
     * @param flag true表示关注，false表示取消关注
     * @return
     */
    @PostMapping(value = "/follow/follow",consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    ZheXingResult followUser(Follow follow, @RequestParam("flag")boolean flag);


    /**
     * 查看某人关注的所有人
     * @param userId
     * @return
     */
    @GetMapping("/follow/following")
    ZheXingResult searchAllFollow(@RequestParam("userId") Long userId);


    /**
     * 查看某人的粉丝
     * @param follower
     * @return
     */
    @GetMapping("/follow/follower")
    ZheXingResult searchAllFollowedUser(@RequestParam("follower") Long follower);


    /***            FollowController              ***/


    /**
     * 获取个人主页
     * @param uname
     * @param userId
     * @return
     */
    @GetMapping("/index/person")
    ZheXingResult index(@RequestParam("uname") String uname,@RequestParam("userId") Long userId);


    /**
     * 查询用户 uname和unickname模糊查找
     * @param userId
     * @param user
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/search/getusers")
    ZheXingResult getUser(@RequestParam("userId") Long userId,@RequestParam("user") String user,@RequestParam("start") Long start,@RequestParam("end") Long end);

    /**
     * 推荐好友
     * @return
     */
    @GetMapping("/recommend/user")
    public ZheXingResult recommendUser(@RequestParam("userId") Long userId);


    /***            IPController              ***/


    /**
     * 获取当前服务器的IP和端口
     * @return
     */
    @GetMapping("/websocket/getip")
    ZheXingResult getIPPort();


    /***            MsgController              ***/

    /**
     * 获取历史消息
     * @param userId
     * @return
     */
    @GetMapping("/user/message")
    ZheXingResult getMessage(@RequestParam("userId") Long userId);


    /**
     * 删除历史消息
     * @param key
     * @return
     */
    @DeleteMapping("/user/message")
    ZheXingResult delMessage(@RequestParam("key") String key);


    /***            IPController              ***/

    /**
     * 获取热搜话题
     * @return
     */
    @GetMapping("/tag/hotTag")
    ZheXingResult hotTag();



    /**
     * 查找话题
     * @param tname
     * @return
     */
    @GetMapping("/tag/searchTag")
    ZheXingResult searchTag(@RequestParam("tname") String tname);


}
