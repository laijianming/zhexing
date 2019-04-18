package com.zhexing.sso.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sso.Util.DateJsonValueProcessor;
import com.zhexing.sso.dao.userDao;
import com.zhexing.sso.domain.User;

public class test {
	public void test() throws Exception {
		InputStream inputStream=Resources.getResourceAsStream("mybatis-config.xml"); 
		SqlSessionFactory sessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sessionFactory.openSession();
	//	System.out.println(session);
		userDao dao=session.getMapper(userDao.class);
		System.out.println(dao.isActional("zqh"));
		
}
public void test1(){
	User user=new User();
	user.setCreated(new Date());
	user.setUemail("wdada");
	JsonConfig jsConfig=new JsonConfig();
	jsConfig.registerJsonValueProcessor(java.util.Date.class,new DateJsonValueProcessor("yyyy-MM-dd"));
	JSONObject json=JSONObject.fromObject(user,jsConfig);
	
	
	}
public void test2(){
	Jedis jedis=new Jedis("192.168.1.126", 6379);
	
}
@Test
public void test4(){
	Date date=new Date();
	DateFormat format=new SimpleDateFormat("/yyyy/MM/dd");
	System.out.println(format.format(date));
	
}


}
