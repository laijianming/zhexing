package com.zhexing.sso.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sso.Util.VerificationCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@RestController
public class HelloController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/hello")
    @ResponseBody
    public List hello() throws Exception{
        System.out.println("jdbcTemplate == " + jdbcTemplate);
        System.out.println("重启！");
        return jdbcTemplate.queryForList("select * from testtb");
    }
   

}
