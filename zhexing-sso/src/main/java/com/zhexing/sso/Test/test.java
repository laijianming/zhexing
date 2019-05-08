package com.zhexing.sso.Test;


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
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import org.junit.jupiter.api.Test;
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
public void test4(){
	Date date=new Date();
	DateFormat format=new SimpleDateFormat("/yyyy/MM/dd");
	System.out.println(format.format(date));
}
public void test5() throws SolrServerException, IOException{
	SolrServer server=new HttpSolrServer("http://192.168.134.128:9999/solr/TagCollection");
	SolrInputDocument document=new SolrInputDocument();
	document.addField("id", "test002");
	document.addField("tname", "大保健");
	document.addField("tcreater", "粑粑");
	server.add(document);
	server.commit();
	
}
public void test6() throws SolrServerException{
	SolrServer server=new HttpSolrServer("http://192.168.134.128:9999/solr/TagCollection");
	SolrQuery solrQuery=new SolrQuery();
	solrQuery.setQuery("id:1");
	QueryResponse response=server.query(solrQuery);
	SolrDocumentList solrDocumentList=response.getResults();
	for(SolrDocument solrDocument:solrDocumentList){
		System.out.println(solrDocument.get("tname"));
	}
}
public void test7() throws SolrServerException, IOException{
	String zkHost="129.204.212.52:2191,129.204.212.52:2192,129.204.212.52:2193";
	//String zkHost="192.168.134.128:2181,192.168.134.128:2182,192.168.134.128:2183";
	CloudSolrServer solrServer=new CloudSolrServer(zkHost);
	solrServer.setDefaultCollection("DynamicCollection");
	SolrInputDocument doc=new SolrInputDocument();
	doc.addField("id","tag1");
	doc.addField("user_id","11");
	doc.addField("contents", "达达娃大大大大");
	solrServer.add(doc);
	solrServer.commit();
}
@Test
public void test8() throws SolrServerException{
	String zkHost="129.204.212.52:2191,129.204.212.52:2192,129.204.212.52:2193";
//	String zkHost="192.168.134.128:2181,192.168.134.128:2182,192.168.134.128:2183";
	CloudSolrServer solrServer=new CloudSolrServer(zkHost);
	solrServer.setDefaultCollection("DynamicCollection");
	SolrQuery solrQuery=new SolrQuery();
	solrQuery.setQuery("id:tag1");
	QueryResponse response=solrServer.query(solrQuery,METHOD.POST);
	SolrDocumentList solrDocumentList=response.getResults();
	for(SolrDocument doc:solrDocumentList){
		System.out.println(doc.get("contents"));
	}
}

}
