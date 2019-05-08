package com.zhexing.social.service.impl;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.social.enums.SocialEnum;
import com.zhexing.social.service.CommentService;
import com.zhexing.social.utils.JsonUtils;
import com.zhexing.social.dao.CommentDao;
import com.zhexing.social.dao.RedisDao;
import com.zhexing.social.dao.UserDao;
import com.zhexing.social.pojo.Comment;
import com.zhexing.social.pojo.DynUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao commentDao;

    @Autowired
    RedisDao redisDao;

    @Autowired
    UserDao userDao;

    /**
     * 动态评论
     * @param comment
     * @return
     */
    @Override
    public ZheXingResult dynamicComment(Comment comment,String tname) {

        // 1、将评论插入到数据库中
        comment.setCommentTime(new Date());
        Long aLong = commentDao.publishComment(comment);

        // 2、将评论添加到对应的缓存上 和 动态热评上 和 动态评论id记录上
        if(aLong >= 1){
            String commentId = comment.getCommentId() + "";
            String dynamicId = comment.getDynamicId() + "";
            // 将评论添加到对应的缓存
            redisDao.hput(SocialEnum.DYNAMIC_COMMENT_ + ":" + dynamicId,commentId,JsonUtils.objectToJson(comment),0L);
            // 将评论添加到对应的动态热评上
            redisDao.zadd(SocialEnum.DYNAMIC_HOT_COMMENT_ + ":" + dynamicId,commentId,1.0);
            // 将评论id添加到动态评论id记录上
            redisDao.lpush(SocialEnum.DYNAMIC_COMMENTID_ + ":" + dynamicId,commentId);

            // 3、给对应动态 和 动态话题下增加1的热度
            if(tname != null && !tname.equals("")){
                String[] tnames = tname.split(" ");
                for(int i = 0; i < tnames.length; i ++){
                    // 将话题的热度计数 增加1
                    redisDao.zincrby(SocialEnum.HOT_TAG_COUNT_ + "",tnames[i],1);
                    // 给话题下动态排行查询次数 +1
                    redisDao.zincrby(SocialEnum.TAG_DYNAMIC_ + ":" + tnames[i],comment.getDynamicId()+"",1);
                }
            }
            // 4、给该动态缓存增加5分钟过期时间
            redisDao.updateExpire(SocialEnum.DYNAMIC_ + "" + comment.getDynamicId(), 5 * 60 * 1000L);
        }




        return ZheXingResult.ok(comment);
    }

    /**
     * 动态评论删除
     * @param commentId
     * @param dynamicId
     * @return
     */
    @Override
    public ZheXingResult deleteComment(Long commentId, Long dynamicId,String tname) {

        // 1、删除数据库中的记录
        Long aLong = commentDao.deleteComment(commentId);

        if (aLong >= 1){
            // 2、删除缓存中的相应记录
            redisDao.hdel(SocialEnum.DYNAMIC_COMMENT_ + ":" + dynamicId,commentId + "");
            redisDao.zdel(SocialEnum.DYNAMIC_HOT_COMMENT_ + ":" + dynamicId,commentId + "");
            redisDao.lrem(SocialEnum.DYNAMIC_COMMENTID_ + ":" + dynamicId,commentId + "");
            // 3、给对应的动态 和 动态话题热度 -1
            if(tname != null && !tname.equals("")){
                String[] tnames = tname.split(" ");
                for(int i = 0; i < tnames.length; i ++){
                    // 将话题的热度计数 -1
                    redisDao.zincrby(SocialEnum.HOT_TAG_COUNT_ + "",tnames[i],-1);
                    // 给话题下动态排行查询次数 -1
                    redisDao.zincrby(SocialEnum.TAG_DYNAMIC_ + ":" + tnames[i],commentId+"",-1);
                }
            }
        }

        return ZheXingResult.ok();
    }

    /**
     * 评论点赞处理
     * @param commentId
     * @param userId
     * @param tname
     * @param flag true 为点赞
     * @return
     */
    @Override
    public ZheXingResult likeComment(Long dynamicId,Long commentId, Long userId, String tname,boolean flag) {
        double count;
        Long sadd;
        if(flag){
            // 1、添加点赞到缓存 ，， 之后会有定时任务持久化点赞记录
            sadd = redisDao.sadd(SocialEnum.LIKE_COMMENT + ":" + commentId, userId + "");
            count = 1;
        }else {
            // 1、取消点赞，删除对应缓存
            sadd = redisDao.srem(SocialEnum.LIKE_COMMENT + ":" + commentId, userId + "");
            count = -1;
        }

        // 2、给对应的动态 和 动态话题热度 + count 值
        if(sadd != 0)
            if(tname != null && !tname.equals("")){
                String[] tnames = tname.split(" ");
                for(int i = 0; i < tnames.length; i ++){
                    // 将话题的热度计数 增加1
                    redisDao.zincrby(SocialEnum.HOT_TAG_COUNT_ + "",tnames[i],count);
                    // 给话题下动态排行查询次数 +1
                    redisDao.zincrby(SocialEnum.TAG_DYNAMIC_ + ":" + tnames[i],dynamicId+"",count);
                }
            }

        return ZheXingResult.ok();
    }

    /**
     * 查找前几条的热评
     * @param dynamicId
     * @return
     */
    @Override
    public ZheXingResult searchHotComment(Long userId,Long dynamicId,int start,int n) {
        // 在缓存中找该动态的 start 开始的 n条热评的commentId
        Set commentSet = redisDao.zrevrange(SocialEnum.DYNAMIC_HOT_COMMENT_ + ":" + dynamicId, start, n, false);

        // 根据commentId来去缓存中查询相应的评论
        List list = redisDao.hmultiGet(SocialEnum.DYNAMIC_COMMENT_ + ":" + dynamicId, commentSet);
        ArrayList<Comment> result = new ArrayList<>();
        for(Object o : list){
            Comment comment = JsonUtils.jsonToPojo(o + "", Comment.class);
            supplementUserInfo(comment);
            lcCount(comment,userId);
            result.add(comment);
        }
        return ZheXingResult.ok(result);
    }


    /**
     * 补充发评论用户的信息
     * @param comment
     */
    public void supplementUserInfo(Comment comment){
        List<DynUser> dynUsers = userDao.selectById(comment.getUserId());
        DynUser dynUser = dynUsers.get(0);
        comment.setUname(dynUser.getUname());
        comment.setUnickname(dynUser.getUnickname());
        comment.setUchathead(dynUser.getUchathead());

    }

    /**
     * 封装评论是否点赞，点赞数，评论数
     * lcCount : l like c comment
     * @param comment
     * @param userId 当前用户id
     * @return
     */
    public void lcCount(Comment comment ,Long userId){
        Long commentId = comment.getCommentId();
        comment.setAction(redisDao.sismember(SocialEnum.LIKE_COMMENT + ":" + commentId,userId + ""));
        comment.setLikesCount(redisDao.scard(SocialEnum.LIKE_COMMENT + ":" + commentId));
        comment.setCommentsCount(redisDao.hlen(SocialEnum.COMMENT_COMMENT_ + ":" + commentId));
    }

    /**
     * 查找动态的评论
     * @param dynamicId
     * @param start 从哪开始
     * @param n 几条
     * @param userId
     * @return
     */
    @Override
    public ZheXingResult getDynamicComments(Long dynamicId,int start,int n, Long userId) {

        // 1、查找评论的id
        List commentIds = redisDao.lrange(SocialEnum.DYNAMIC_COMMENTID_ + ":" + dynamicId, start, n);

        // 2、根据评论id查找评论
        ArrayList<Comment> result = new ArrayList<>();
        for (Object commentId : commentIds){
            Comment comment = selectCommentById(Long.parseLong(commentId + ""), dynamicId);
            // 补充发评论者的信息与评论中评论的信息
            supplementUserInfo(comment);
            lcCount(comment,userId);
            result.add(comment);
        }

        return ZheXingResult.ok(result);
    }

    /**
     * 评论评论
     * @param comment
     * @return
     */
    @Override
    public ZheXingResult commentComment(Comment comment) {
        // 1、将评论插入到数据库中
        commentDao.publishComment(comment);
        // 2、将评论添加到缓存中
        redisDao.hput(SocialEnum.COMMENT_COMMENT_ + ":" + comment.getParentId(),comment.getCommentId() + "",JsonUtils.objectToJson(comment),0L);

        return ZheXingResult.ok();
    }

    /**
     * 删除评论评论
     * @param commentId
     * @return
     */
    @Override
    public ZheXingResult deleteCComment(Long commentId,Long parentId) {
        // 1、删除数据库记录
        commentDao.deleteComment(commentId);
        // 2、删除缓存
        redisDao.hdel(SocialEnum.COMMENT_COMMENT_ + ":" + parentId,commentId + "");

        return ZheXingResult.ok();
    }

    /**
     * 查看评论的所有评论
     * @param parentId
     * @return
     */
    @Override
    public ZheXingResult getCComments(Long parentId,Long userId) {
        // 去缓存中查该评论的所有评论
        List cComments = redisDao.hvalues(SocialEnum.COMMENT_COMMENT_ + ":" + parentId);
        ArrayList<Comment> result = new ArrayList<>();
        for(Object o : cComments){
            Comment comment = JsonUtils.jsonToPojo(o + "", Comment.class);
            supplementUserInfo(comment);
            lcCount(comment,userId);
            result.add(comment);
        }
        return ZheXingResult.ok(result);
    }


    /**
     * 根据评论id查找评论
     * @param commentId
     * @return
     */
    public Comment selectCommentById(Long commentId,Long dynamicId){
        // 1、先查缓存中是否有该动态，有则返回
        Object s = redisDao.hget(SocialEnum.DYNAMIC_ + "" + dynamicId, commentId + "");
        if(s != null && !s.equals("null"))
            return JsonUtils.jsonToPojo(s + "",Comment.class);
        // 2、查数据库中该动态
        return commentDao.selectById(commentId);
    }

}
