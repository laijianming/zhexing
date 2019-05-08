package com.zhexing.search.dao;

import java.util.List;

import com.zhexing.search.domain.dynamicItem;

public interface DynamicSearchDao {

	/**
	 * 将数据库所有的组成dynamicItem的对象查找出
	 * @return 
	 */
	List<dynamicItem> getDynaList();
}

