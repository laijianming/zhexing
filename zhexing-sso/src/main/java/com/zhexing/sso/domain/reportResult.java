package com.zhexing.sso.domain;

import java.util.List;

import org.apache.commons.io.filefilter.IOFileFilter;

public class reportResult {
	//记录集合
	private List<report_detail> reports;
	//记录的总页数
	private Long pageCount;
	//总记录条数
	private Long reportCount;
	//当前页数
	private Long curpage;
	//每一页要显示的记录条数
	private int pagesize;
	//数据开始索引
	private Long index;
	
	public List<report_detail> getReports() {
		return reports;
	}
	public void setReports(List<report_detail> reports) {
		this.reports = reports;
	}
	public Long getPageCount() {
		return pageCount;
	}
	public void setPageCount(Long pageCount) {
		this.pageCount = pageCount;
	}
	public Long getReportCount() {
		return reportCount;
	}
	public void setReportCount(Long reportCount) {
		this.reportCount = reportCount;
	}
	public Long getCurpage() {
		return curpage;
	}
	public void setCurpage(Long curpage) {
		this.curpage = curpage;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public Long getIndex() {
		return index;
	}
	public void setIndex(Long index) {
		this.index = index;
	}
	public reportResult(Long reportCount, Long curpage, int pagesize) {
		super();
		this.reportCount = reportCount;
		this.curpage = curpage;
		this.pagesize = pagesize;
		this.pageCount=reportCount/pagesize;
		if(curpage>pageCount){
			this.curpage=pageCount;
		}
		if(curpage<1){
			this.curpage=new Long("1");
		}
	}
	public reportResult() {
		super();
	}
	
	
	
}
