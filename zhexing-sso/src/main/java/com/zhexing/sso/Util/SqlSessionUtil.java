package com.zhexing.sso.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//获取sqlsession工具类
public class SqlSessionUtil {

	public static SqlSession getSqlSession(){
		InputStream in;
		try {
			in = Resources.getResourceAsStream("mybatis-config.xml");			
			SqlSessionFactory sFactory=new SqlSessionFactoryBuilder().build(in);
			SqlSession session=sFactory.openSession();
			return session;
		} catch (IOException e) {
			throw new RuntimeException("sqlsession获取异常");
		}
		
	
}
}
