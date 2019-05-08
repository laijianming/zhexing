package com.zhexing.search.dao.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.zhexing.search.dao.DynamicCRUDDao;
import com.zhexing.search.domain.dynamicItem;
import com.zhexing.search.domain.dynamicResult;

public class DynamicCRUDDaoImpl implements DynamicCRUDDao {
private SolrServer solrServer=new HttpSolrServer("http://192.168.134.128:9999/solr/DynamicCollection");


@Override
	public dynamicResult search(SolrQuery solrQuery) throws SolrServerException, ParseException {
		//根据查询条件去查询索引库
		QueryResponse response=solrServer.query(solrQuery);
		//动态列表
		List<dynamicItem> itemList=new ArrayList<dynamicItem>();
		//返回值对象
		dynamicResult result=new dynamicResult();
		//取查询结果
		SolrDocumentList documentList=response.getResults();
		//定义一个时间转换器
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//取高亮显示
		Map<String, Map<String, List<String>>> highlighting=response.getHighlighting();
		for(SolrDocument doc:documentList){
			String publish_time=(String) doc.get("publish_time");
			Date date=format.parse(publish_time);
			dynamicItem item=new dynamicItem();
			String contents="";
			//取高亮显示的结果
			List<String> list=highlighting.get(doc.get("id")).get("contents");
			if(list!=null&&list.size()>0){
				contents=list.get(0);
			}else{
				contents=(String) doc.get("contents");
			}
			//将查询到动态数据填充到dynamicItem对象中
			item.setContents(contents);
			item.setDynamic_id(Integer.parseInt((String) doc.get("id")));
			item.setImages((String) doc.get("images"));
			item.setPublish_time(date);
			item.setUser_id(Long.parseLong((String) doc.get("user_id")));
			item.setUser_uname((String) doc.get("user_uname"));
			item.setUser_unickname((String) doc.get("user_unickname"));
			//将每个dynamicItem添加到List集合
			itemList.add(item);
		}
		//取查询结果动态对象集合
		result.setItemlist(itemList);
		//去查询结果的总记录数
		result.setRecordCount(documentList.getNumFound());
		return result;
	}
@Override
public void AddDynamic(dynamicItem item) throws Exception {
	//定义时间解析器
	DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//获取solr输入流
	SolrInputDocument inputDocument=new SolrInputDocument();
	//为输入流填充信息
	inputDocument.addField("id", item.getDynamic_id());
	inputDocument.addField("user_id", item.getUser_id());
	inputDocument.addField("user_uname", item.getUser_uname());
	String publish_time=format.format(item.getPublish_time());
	inputDocument.addField("publish_time",publish_time);
	inputDocument.addField("contents", item.getContents());
	inputDocument.addField("images", item.getImages());
	inputDocument.addField("user_unickname",item.getUser_unickname());
	solrServer.add(inputDocument);
	solrServer.commit();
}
@Override
public void DeleteDynamicDocById(Integer id) throws Exception {
	solrServer.deleteById(id+"");
	solrServer.commit();
}
}
