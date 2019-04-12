package com.zhexing.sociality.controller;


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

    @RequestMapping("/hello")
    public List hello(){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from tb_dynamic");
        return maps;
    }


}
