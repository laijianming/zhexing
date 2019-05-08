package com.zhexing.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import com.zhexing.search.domain.dynamicItem;
import com.zhexing.search.domain.dynamicResult;

public interface DynamicCRUDDao {
	/**
	 * 根据查询对象，返回dynamicResult对象
	 * @param solrQuery
	 * @return
	 * @throws Exception
	 */
	dynamicResult search(SolrQuery solrQuery) throws Exception;
	/**
	 * 根据传递过来的dynamicItem对象向索引库新增动态数据
	 * @param item
	 */
	void AddDynamic(dynamicItem item) throws Exception;
	/**
	 * 根据传过来的动态的id来删除索引库相应的动态记录
	 * @param id 动态的id
	 */
	void DeleteDynamicDocById(Integer id) throws Exception;
}
