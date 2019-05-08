package com.zhexing.search.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;


import org.apache.ibatis.session.SqlSession;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.search.dao.DynamicCRUDDao;
import com.zhexing.search.dao.DynamicSearchDao;
import com.zhexing.search.dao.TagCRUDDao;
import com.zhexing.search.dao.TagSearchDao;
import com.zhexing.search.dao.impl.DynamicCRUDDaoImpl;
import com.zhexing.search.dao.impl.DynamicCRUDDaoImpl_cloud;
import com.zhexing.search.dao.impl.TagCRUDDaoImpl;
import com.zhexing.search.dao.impl.TagCRUDDaoImpl_cloud;
import com.zhexing.search.domain.TagItem;
import com.zhexing.search.domain.TagResult;
import com.zhexing.search.domain.dynamicItem;
import com.zhexing.search.domain.dynamicResult;
import com.zhexing.search.service.SearchService;
import com.zhexing.sso.Util.DateJsonValueProcessor;
import com.zhexing.sso.Util.SqlSessionUtil;

public class SearchServiceImpl implements SearchService {
private DynamicCRUDDao dynamicCRUDDao=new DynamicCRUDDaoImpl_cloud();
private TagCRUDDao tagCRUDDao=new TagCRUDDaoImpl_cloud();
	@Override
	public ZheXingResult importAllDyAndTag() throws Exception {
		try {
		//获取与动态索引库的客户端对象(单机版)
//		SolrServer DynamicsolrServer=new HttpSolrServer("http://192.168.134.128:9999/solr/DynamicCollection");
		//zookeeper的集群ip+端口
		String zkHost="129.204.212.52:2191,129.204.212.52:2192,129.204.212.52:2193";	
//		String zkHost="192.168.134.128:2181,192.168.134.128:2182,192.168.134.128:2183";
		CloudSolrServer DynamicsolrServer=new CloudSolrServer(zkHost); 
		DynamicsolrServer.setDefaultCollection("DynamicCollection");
		//获取sqlsession对象
		SqlSession session=SqlSessionUtil.getSqlSession();
		//获取DynamicSearchDao对象
		DynamicSearchDao DyDao=session.getMapper(DynamicSearchDao.class);
		//获取数据库中所有的动态数据
		List<dynamicItem> dynamicItems=DyDao.getDynaList();
		//定义时间格式对象，方便后续的将date对象按我们希望的时间格式存入索引库
        DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
		//向动态索引库导入动态数据
		for(dynamicItem item:dynamicItems){
			//获取solr数据输入流
			SolrInputDocument doc=new SolrInputDocument();
			//将动态对象中的date对象转换为用字符串表示时间
			String publish_time=format2.format(item.getPublish_time());
			//将数据填充入输入流
			doc.addField("id", item.getDynamic_id());
			doc.addField("user_id", item.getUser_id());
			doc.addField("publish_time", publish_time);
			doc.addField("contents", item.getContents());
			doc.addField("images", item.getImages());
			doc.addField("user_unickname", item.getUser_unickname());
			doc.addField("user_uname", item.getUser_uname());
			//将solr输入流填入solrservice
			DynamicsolrServer.add(doc);
		}
			//提交！
				DynamicsolrServer.commit();
				
				//获取TagSearchDao对象
				TagSearchDao TDao =session.getMapper(TagSearchDao.class);
				//获取数据库所有话题对象
				List<TagItem> tagItems=TDao.getItemList();
				//获取solr话题索引库的客户端
				//SolrServer TagSlorServer=new HttpSolrServer("http://192.168.134.128:9999/solr/TagCollection");
				//将话题数据添加到solr话题索引库
				//直接将cloudsolrserver的默认索引库改为TagCollection就行
				DynamicsolrServer.setDefaultCollection("TagCollection");
				
				for(TagItem item:tagItems){
					SolrInputDocument doc=new SolrInputDocument();
					String tcreate_time=format2.format(item.getTcreate_time());
					System.out.println("tcreate_time:"+tcreate_time);
					doc.addField("id", item.getTag_id());
					doc.addField("tname", item.getTname());
					doc.addField("tcreater", item.getTcreater());
					doc.addField("tcreate_time", tcreate_time);
					DynamicsolrServer.add(doc);
					//TagSlorServer.add(doc);
				}
				//TagSlorServer.commit();
				DynamicsolrServer.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			return ZheXingResult.build(500, e.getMessage());
		}
		return ZheXingResult.ok();
	}

	@Override
	public dynamicResult searchDynamic(String queryString, int page, int rows) throws Exception {
		SolrQuery solrQuery=new SolrQuery();
		//添加查询条件
		solrQuery.setQuery(queryString);
		//设置默认搜索域
		solrQuery.set("df","Dynamic_keywords");
		//设置查询开始页数
		solrQuery.setStart((page-1)*rows);
		//设置每页的数据数
		solrQuery.setRows(rows);
		//设置高亮显示
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("contents");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		dynamicResult result=dynamicCRUDDao.search(solrQuery);
		//确定返回值的总页数
		long recordCount=result.getRecordCount();
		long pageCount=recordCount/rows;
		if(recordCount%rows>0){
			pageCount++;
		}
		result.setPageCount(pageCount);
		result.setRecordCount(recordCount);
		result.setCurpage(page);
		return result;
	}

	@Override
	public ZheXingResult AddDynamic(dynamicItem item) {
		try {
			//执行动态索引库添加操作
			dynamicCRUDDao.AddDynamic(item);
			return ZheXingResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ZheXingResult.build(500, e.getMessage());
		}
	}

	@Override
	public void DeleteDynamicDocById(Integer id) throws Exception {
		dynamicCRUDDao.DeleteDynamicDocById(id);
	}

	@Override
	public TagResult searchTag(String queryString, int page, int rows)
			throws Exception {
		//获取查询对象
		SolrQuery solrQuery=new SolrQuery();
		//设置查询条件
		solrQuery.setQuery(queryString);
		//设置默认搜索域
		solrQuery.set("df", "Tag_keywords");
		//设置开始页数
		solrQuery.setStart((page-1)*rows);
		//设置页面数据数量
		solrQuery.setRows(rows);
		//执行查询
		TagResult result=tagCRUDDao.search(solrQuery);
		long recordCount=result.getRecordCount();
		long pageCount=recordCount/rows;
		if(recordCount%rows>0){
			pageCount++;
		}
		//设置返回值总页数
		result.setPageCount((int)pageCount);
		result.setRecordCount(recordCount);
		result.setCurPage(page);
		return result;
	}

	@Override
	public void AddTag(TagItem item) throws Exception {
		tagCRUDDao.AddTag(item);
	}

	@Override
	public void DeleteTagById(Integer id) throws Exception {
		tagCRUDDao.DeleteTagById(id);
	}

}
