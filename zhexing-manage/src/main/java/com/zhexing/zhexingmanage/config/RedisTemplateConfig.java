package com.zhexing.zhexingmanage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * StringRedisTemplate与RedisTemplate
 * 两者的关系是StringRedisTemplate继承RedisTemplate。
 *
 * 两者的数据是不共通的；也就是说StringRedisTemplate只能管理StringRedisTemplate里面的数据，RedisTemplate只能管理RedisTemplate中的数据。
 *
 * SDR默认采用的序列化策略有两种，一种是String的序列化策略，一种是JDK的序列化策略。
 *
 * StringRedisTemplate默认采用的是String的序列化策略，保存的key和value都是采用此策略序列化保存的。
 *
 * RedisTemplate默认采用的是JDK的序列化策略，保存的key和value都是采用此策略序列化保存的
 */
@Configuration
public class RedisTemplateConfig {


    @Configuration
    public class RedisConfigurtion {

        @Autowired
        private RedisTemplate redisTemplate;

        /**
         *
         * @return
         */
        @Bean
        public RedisTemplate<String, Object> stringSerializerRedisTemplate() {
            RedisSerializer<String> stringSerializer = new StringRedisSerializer();
            redisTemplate.setKeySerializer(stringSerializer);
            redisTemplate.setValueSerializer(stringSerializer);
            redisTemplate.setHashKeySerializer(stringSerializer);
            redisTemplate.setHashValueSerializer(stringSerializer);
            return redisTemplate;
        }

    }


}
