package com.zhexing.sso.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestConnectSql {


    @Test
    public void testConnect(){
        DataSource dataSource = new DruidDataSource();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from testtb");
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()){
                System.out.println(resultSet.first());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
