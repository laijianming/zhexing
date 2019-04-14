package com.zhexing.sociality.dao;

import com.zhexing.sociality.pojo.DynamicSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存操作
 */
@Component
public class RedisDao {


    @Autowired
    RedisTemplate redisTemplate;


    /**
     * String结构添加数据
     * @param K
     * @param V
     * @param millisecond
     * @return
     */
    public boolean put(String K,String V,Long millisecond){
        redisTemplate.opsForValue().append(K,V);
        expire(K,millisecond);
        return true;
    }


    /**
     * hash结构添加缓存
     * @param H
     * @param K
     * @param V
     * @param millisecond
     * @return
     */
    public boolean put(String H,String K,String V,Long millisecond){
        redisTemplate.opsForHash().put(H,K,V);
        expire(K,millisecond);
        return true;
    }

    /**
     * List结构添加缓存
     * @param K
     * @param millisecond
     * @param V
     * @return
     */
    public boolean putList(String K,Long millisecond,String ... V){
        redisTemplate.opsForList().rightPush(K,V);
        expire(K,millisecond);
        return true;
    }

    /**
     * 删除缓存
     * @param K
     * @return
     */
    public boolean deleteCache(String K){
        redisTemplate.delete(K);
        return true;
    }


    /**
     * 添加Zset结构缓存
     *   默认值为 1
     * @param K
     * @param V
     * @param value 该缓存的计数值
     * @return
     */
    public void zadd(String K,String V,Double value){
        redisTemplate.opsForZSet().add(K,V,value);
    }

    public boolean zadd(String K,String V){
        zadd(K,V,1.0);
        return true;
    }

    /**
     * Zset返回有序集中指定区间内的成员，通过索引，分数从高到底
     * @param K
     * @param Start
     * @param end
     * @param withscores
     * @return
     */
    public Set zrevrange(String K, int Start, int end, boolean withscores){

        return  withscores?redisTemplate.opsForZSet().reverseRangeWithScores(K,Start,end)
                :redisTemplate.opsForZSet().reverseRange(K, Start, end);
    }

    /**
     * Zset有序集合中对指定成员的分数加上增量 increment
     * @param K
     * @param V
     * @param increment
     * @return
     */
    public boolean zincrby(String K,String V,double increment){
        redisTemplate.opsForZSet().incrementScore(K,V,increment);
        return true;
    }

    /**
     * Zset结构缓存 返回有序集中，成员的分数值
     * @param K
     * @param V
     * @return
     */
    public Double zscore(String K,String V){
        return redisTemplate.opsForZSet().score(K, V);
    }

    /**
     * 删除Zset结构缓存
     * @param K
     * @param V
     * @return
     */
    public boolean zdel(String K,String V){
        redisTemplate.opsForZSet().remove(K,V);
        return true;
    }



    /**
     * 设置有效时间
     * @param K
     * @param millisecond
     */
    public void expire(String K,Long millisecond){
        if(millisecond != 0){
            redisTemplate.expire(K,millisecond,TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 给缓存的有效期增加或修改 millisecond 毫秒
     * @param K
     * @param millisecond
     */
    public void updateExpire(String K,Long millisecond){
        Long expire = redisTemplate.getExpire(K);
        Long time = (expire * 1000) + millisecond;
        expire(K,time);
    }

    /**
     * 给String的值增加len大小
     * @param K
     * @param len
     */
    public void incr(String K,Long len){
        redisTemplate.opsForValue().increment(K,len);
    }

    /**
     * 给String的值减小len大小
     * @param K
     * @param len
     */
    public void decr(String K,Long len){
        redisTemplate.opsForValue().decrement(K,len);
    }
}
