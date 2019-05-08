package com.zhexing.search.dao;

import java.util.List;

import com.zhexing.search.domain.TagItem;

public interface TagSearchDao {
	/**
	 * 查询出数据库中所有TagItem的信息
	 * @return
	 */
List<TagItem> getItemList();
}
