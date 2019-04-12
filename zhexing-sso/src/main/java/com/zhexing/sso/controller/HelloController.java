package com.zhexing.sso.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/hello")
    @ResponseBody
    public List hello() throws Exception{
        System.out.println("jdbcTemplate == " + jdbcTemplate);
        return jdbcTemplate.queryForList("select * from testtb");
    }


}
