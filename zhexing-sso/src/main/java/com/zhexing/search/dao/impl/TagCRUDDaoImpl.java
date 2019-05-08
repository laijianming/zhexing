package com.zhexing.search.dao.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.zhexing.search.dao.TagCRUDDao;
import com.zhexing.search.domain.TagItem;
import com.zhexing.search.domain.TagResult;

public class TagCRUDDaoImpl implements TagCRUDDao {
//获得TagCollection索引库的客户端
private SolrServer solrServer=new HttpSolrServer("http://192.168.134.128:9999/solr/TagCollection");


	@Override
	public TagResult search(SolrQuery solrQuery) throws Exception {
		//创建返回值对象
		TagResult result=new TagResult();
		//获得查询响应对象
		QueryResponse response=solrServer.query(solrQuery);
		//获得查询结果集
		SolrDocumentList solrDocumentList=response.getResults();
		//设置查询结果的总记录数
		result.setRecordCount(solrDocumentList.getNumFound());
		//tag话题数据集合
		List<TagItem> itemlist=new ArrayList<TagItem>();
		//创建一个时间日期转换器
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获得高亮显示（目前还不知道需不需要高亮，所以先不加了）
		Map<String, Map<String, List<String>>> map=response.getHighlighting();
		for(SolrDocument doc:solrDocumentList){
			//将得到的代表时间的字符串按指定格式转换为date类型的时间对象
			String date=(String) doc.get("tcreate_time");
			Date tcreate_time=format.parse(date);
			//创建TagItem对象并填充数据
			TagItem item=new TagItem();
			item.setTag_id(Integer.parseInt((String) doc.get("id")));
			item.setTname((String) doc.get("tname"));
			item.setTcreater((String) doc.get("tcreater"));
			item.setTcreate_time(tcreate_time);
			itemlist.add(item);
		}
		result.setItemlist(itemlist);
		
		return result;
	}

	@Override
	public void AddTag(TagItem item) throws Exception {
		//定义时间日期转换器
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SolrInputDocument doc=new SolrInputDocument();
		doc.addField("id", item.getTag_id());
		doc.addField("tname", item.getTname());
		doc.addField("tcreater", item.getTcreater());
		//将date类型数据转换为string再存入索引库
		String tcreate_time=format.format(item.getTcreate_time());
		doc.addField("tcreate_time",tcreate_time);
		solrServer.add(doc);
		solrServer.commit();
	}

	@Override
	public void DeleteTagById(Integer id) throws Exception {
		solrServer.deleteById(id+"");
		solrServer.commit();
	}

}
