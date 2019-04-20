package com.zhexing.sociality.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
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
     * String结构获取换缓存值
     * @param K
     * @return
     */
    public String get(String K){
        String result = redisTemplate.opsForValue().get(K) + "";
        return result;
    }


    /**
     * hash结构添加缓存
     * @param H
     * @param K
     * @param V
     * @param millisecond
     * @return
     */
    public boolean hput(String H,String K,String V,Long millisecond){
        redisTemplate.opsForHash().put(H,K,V);
        expire(K,millisecond);
        return true;
    }


    /**
     * hash结构删除缓存
     * @param H
     * @param K
     * @return
     */
    public Long hdel(String H,String K){
        return redisTemplate.opsForHash().delete(H,K);
    }


    /**
     * List结构添加缓存
     * @param K
     * @param millisecond
     * @param V
     * @return
     */
    public boolean putList(String K,Long millisecond,String V){
        redisTemplate.opsForList().rightPush(K,V);
        expire(K,millisecond);
        return true;
    }
    public boolean putAllList(String K,Long millisecond,String[] V){
        redisTemplate.opsForList().rightPushAll(K,V);
        expire(K,millisecond);
        return true;
    }

    /**
     * List结构取出值
     * @param K
     * @param start
     * @param end
     * @return
     */
    public List lrange(String K,long start,long end){
        List range = redisTemplate.opsForList().range(K, start, end);
        return range;

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
        // withscores true 返回 v 和 score ； false 不返回 score
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
     * 获取Zset结构下K的条数
     * @param K
     * @return
     */
    public Long zcard(String K){
        return redisTemplate.opsForZSet().zCard(K);
    }


    /**
     * 添加Set结构缓存
     * @param K
     * @param V
     * @return
     */
    public Long sadd(String K,String V){
        Long add = redisTemplate.opsForSet().add(K, V);
        return add;
    }

    public boolean sadd(String K,String ... V){
        redisTemplate.opsForSet().add(K,V);
        return true;
    }

    /**
     * 删除Set结构缓存
     * @param K
     * @param V
     * @return
     */
    public Long srem(String K,String V){
        return redisTemplate.opsForSet().remove(K,V);
    }


    /**
     * 获取Set结构缓存值
     * @param K
     * @return
     */
    public Set smembers(String K){
        return redisTemplate.opsForSet().members(K);
    }


    /**
     * 设置有效时间
     * @param K
     * @param millisecond
     */
    public void expire(String K,Long millisecond){
        if(millisecond != null && millisecond != 0){
            redisTemplate.expire(K,millisecond,TimeUnit.MILLISECONDS);
        }
    }


    /**
     * 给缓存的有效期增加或减少 millisecond 毫秒
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
