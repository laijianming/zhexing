package com.zhexing.sso.domain;

import java.util.Date;

public class report {
private long reporter_id;
private String report_content;
private Integer report_type;
private long reported_id;
private Date reported_time;
public long getReporter_id() {
	return reporter_id;
}
public void setReporter_id(long reporter_id) {
	this.reporter_id = reporter_id;
}
public String getReport_content() {
	return report_content;
}
public void setReport_content(String report_content) {
	this.report_content = report_content;
}
public Integer getReport_type() {
	return report_type;
}
public void setReport_type(Integer report_type) {
	this.report_type = report_type;
}
public long getReported_id() {
	return reported_id;
}
public void setReported_id(long reported_id) {
	this.reported_id = reported_id;
}
public Date getReported_time() {
	return reported_time;
}
public void setReported_time(Date reported_time) {
	this.reported_time = reported_time;
}
}
 