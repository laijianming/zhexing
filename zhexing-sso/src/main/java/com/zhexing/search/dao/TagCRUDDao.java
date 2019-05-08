package com.zhexing.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import com.zhexing.search.domain.TagItem;
import com.zhexing.search.domain.TagResult;

public interface TagCRUDDao {
	
	/**
	 * 根据solrQuery对象查询话题数据，并返回TagResult对象
	 * @param solrQuery
	 * @return
	 */
TagResult search(SolrQuery solrQuery) throws Exception;
/**
 * 将传过来的item话题对象更新入索引库
 * @param item
 */
void AddTag(TagItem item) throws Exception;
/**
 * 根据话题的id值从索引库中删除话题
 * @param id
 */
void DeleteTagById(Integer id) throws Exception;

}
