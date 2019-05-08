package com.zhexing.search.service;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.search.domain.TagItem;
import com.zhexing.search.domain.TagResult;
import com.zhexing.search.domain.dynamicItem;
import com.zhexing.search.domain.dynamicResult;

public interface SearchService {
	
	/**
	 * 向solr索引库导入所有的动态和话题
	 * @return
	 */
ZheXingResult importAllDyAndTag() throws Exception;
/**
 * 接受查询条件以及分页条件，创建一个solrQuery对象。指定查询条件、分页条件、默认搜索域、高亮显示，调用dao层进行查询，根据查询信息确定页数
 * @param queryString
 * @param page
 * @param rows
 * @return
 */
dynamicResult searchDynamic(String queryString,int page,int rows) throws Exception;
/**
 * 接受item对象 并调用dao层进行添加动态操作
 * @param item
 * @return
 */
ZheXingResult AddDynamic(dynamicItem item);
/**
 * 根据控制器传过来的动态的id调用dao层进行动态的删除。
 * @param id
 */
void DeleteDynamicDocById(Integer id) throws Exception;
/**
 * 接受查询条件以及分页条件，创建一个solrQuery对象。指定查询条件、分页条件、默认搜索域，调用dao层进行查询，根据查询信息确定页数
 * @param queryString
 * @param page
 * @param rows
 * @return
 * @throws Exception
 */
TagResult searchTag(String queryString,int page,int rows) throws Exception;
/**
 * 接受item 并调用dao层进行话题的添加
 * @param item
 */
void AddTag(TagItem item) throws Exception;
/**
 * 根据话题的id，从索引库删除该话题记录
 * @param id
 * @throws Exception
 */
void DeleteTagById(Integer id) throws Exception;
}
