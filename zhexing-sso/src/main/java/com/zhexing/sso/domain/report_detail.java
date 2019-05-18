package com.zhexing.sso.domain;

import java.util.Date;

public class report_detail {
	private String reporter_name;
	private String report_content;
	private String report_desc;
	private String reported_name;
	private Date reported_time;
	private int reported_number;
	
	public int getReported_num() {
		return reported_number;
	}
	public void setReported_num(int reported_num) {
		this.reported_number = reported_num;
	}
	public String getReporter_name() {
		return reporter_name;
	}
	public void setReporter_name(String reporter_name) {
		this.reporter_name = reporter_name;
	}
	public String getReport_content() {
		return report_content;
	}
	public void setReport_content(String report_content) {
		this.report_content = report_content;
	}
	public String getReport_desc() {
		return report_desc;
	}
	public void setReport_desc(String report_desc) {
		this.report_desc = report_desc;
	}
	public String getReported_name() {
		return reported_name;
	}
	public void setReported_name(String reported_name) {
		this.reported_name = reported_name;
	}
	public Date getReported_time() {
		return reported_time;
	}
	public void setReported_time(Date reported_time) {
		this.reported_time = reported_time;
	}
	
}
