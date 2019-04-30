package com.zhexing.sociality.service.impl;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.dao.CommentDao;
import com.zhexing.sociality.dao.RedisDao;
import com.zhexing.sociality.enums.SocialEnum;
import com.zhexing.sociality.pojo.Comment;
import com.zhexing.sociality.service.CommentService;
import com.zhexing.sociality.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao commentDao;

    @Autowired
    RedisDao redisDao;

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
            redisDao.hput(SocialEnum.DYNAMIC_COMMENT_ + dynamicId,commentId,JsonUtils.objectToJson(comment),0L);
            // 将评论添加到对应的动态热评上
            redisDao.zadd(SocialEnum.DYNAMIC_HOT_COMMENT_ + dynamicId,commentId,1.0);
            // 将评论id添加到动态评论id记录上
            redisDao.lpush(SocialEnum.DYNAMIC_COMMENTID_ + dynamicId,commentId);

            // 3、给对应动态 和 动态话题下增加1的热度
            if(tname != null && !tname.equals("")){
                String[] tnames = tname.split(" ");
                for(int i = 0; i < tnames.length; i ++){
                    // 将话题的热度计数 增加1
                    redisDao.zincrby(SocialEnum.HOT_TAG_COUNT_ + "",tnames[i],1);
                    // 给话题下动态排行查询次数 +1
                    redisDao.zincrby(SocialEnum.TAG_DYNAMIC_ + tnames[i],comment.getDynamicId()+"",1);
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
            redisDao.hdel(SocialEnum.DYNAMIC_COMMENT_ + "" + dynamicId,commentId + "");
            redisDao.zdel(SocialEnum.DYNAMIC_HOT_COMMENT_ + "" + dynamicId,commentId + "");
            redisDao.lrem(SocialEnum.DYNAMIC_COMMENTID_ + "" + dynamicId,commentId + "");
            // 3、给对应的动态 和 动态话题热度 -1
            if(tname != null && !tname.equals("")){
                String[] tnames = tname.split(" ");
                for(int i = 0; i < tnames.length; i ++){
                    // 将话题的热度计数 -1
                    redisDao.zincrby(SocialEnum.HOT_TAG_COUNT_ + "",tnames[i],-1);
                    // 给话题下动态排行查询次数 -1
                    redisDao.zincrby(SocialEnum.TAG_DYNAMIC_ + tnames[i],commentId+"",-1);
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
     * @param flag 1 表示 点赞； 0（其他）表示取消点赞 -1
     * @return
     */
    @Override
    public ZheXingResult likeComment(Long commentId, Long userId, String tname,int flag) {
        double count;
        Long sadd;
        if(flag == 1){
            // 1、添加点赞到缓存 ，， 之后会有定时任务持久化点赞记录
            sadd = redisDao.sadd(SocialEnum.LIKE_DYNAMIC_ + "" + commentId, userId + "");
            count = 1;
        }else {
            // 1、取消点赞，删除对应缓存
            sadd = redisDao.srem(SocialEnum.LIKE_DYNAMIC_ + "" + commentId, userId + "");
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
                    redisDao.zincrby(SocialEnum.TAG_DYNAMIC_ + tnames[i],commentId+"",count);
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
    public ZheXingResult searchHotComment(Long dynamicId,int start,int n) {
        // 在缓存中找该动态的 start 开始的 n条热评的commentId
        Set commentSet = redisDao.zrevrange(SocialEnum.DYNAMIC_HOT_COMMENT_ + "" + dynamicId, start, n, false);

        // 根据commentId来去缓存中查询相应的评论
        List list = redisDao.hmultiGet(SocialEnum.DYNAMIC_COMMENT_ + "" + dynamicId, commentSet);
        return ZheXingResult.ok(list);
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
        List commentIds = redisDao.lrange(SocialEnum.DYNAMIC_COMMENTID_ + "" + dynamicId, start, n);

        // 2、根据评论id查找评论
        // 查缓存
        // 查数据库

        return ZheXingResult.ok();
    }

}
