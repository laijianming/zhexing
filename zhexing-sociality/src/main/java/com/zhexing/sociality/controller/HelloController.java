package com.zhexing.sociality.controller;


import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sociality.dao.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HelloController {


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RedisDao redisDao;

    @RequestMapping("/hello")
    public List hello(){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from tb_dynamic");
        return maps;
    }

    @RequestMapping("/testredis")
    public ZheXingResult testRedis(){
        redisDao.zincrby("test","his",-1.5);
        return ZheXingResult.ok();
    }


    @RequestMapping("/redis/test")
    public ZheXingResult redisTest(){
        System.out.println("start ---");
        boolean redisdao_test = redisDao.put("redisdao test", "ok!!!", 0L);
        System.out.println("end ---");
        return redisdao_test?ZheXingResult.ok():ZheXingResult.build(500,"redisdao fallback!!");
    }

}
