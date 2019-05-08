package com.zhexing.search.domain;

import java.util.List;

public class TagResult {
private List<TagItem> itemlist;
private long recordCount;
private int pageCount;
private int curPage;
public List<TagItem> getItemlist() {
	return itemlist;
}
public void setItemlist(List<TagItem> itemlist) {
	this.itemlist = itemlist;
}
public long getRecordCount() {
	return recordCount;
}
public void setRecordCount(long recordCount) {
	this.recordCount = recordCount;
}
public int getPageCount() {
	return pageCount;
}
public void setPageCount(int pageCount) {
	this.pageCount = pageCount;
}
public int getCurPage() {
	return curPage;
}
public void setCurPage(int curPage) {
	this.curPage = curPage;
}
}
