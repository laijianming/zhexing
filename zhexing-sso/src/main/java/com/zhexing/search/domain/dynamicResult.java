package com.zhexing.search.domain;

import java.util.List;

public class dynamicResult {
//动态列表
private List<dynamicItem> itemlist;
//动态记录数
private long recordCount;
//总页数
private long pageCount;
//当前页面
private long curpage;
public List<dynamicItem> getItemlist() {
	return itemlist;
}
public void setItemlist(List<dynamicItem> itemlist) {
	this.itemlist = itemlist;
}
public long getRecordCount() {
	return recordCount;
}
public void setRecordCount(long recordCount) {
	this.recordCount = recordCount;
}
public long getPageCount() {
	return pageCount;
}
public void setPageCount(long pageCount) {
	this.pageCount = pageCount;
}
public long getCurpage() {
	return curpage;
}
public void setCurpage(long curpage) {
	this.curpage = curpage;
}


}
