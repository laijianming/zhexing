package com.zhexing.search.domain;

import java.util.Date;

public class TagItem {
private int tag_id;
private String tname;
private String tcreater;
private Date tcreate_time;

public int getTag_id() {
	return tag_id;
}
public void setTag_id(int tag_id) {
	this.tag_id = tag_id;
}
public String getTname() {
	return tname;
}
public void setTname(String tname) {
	this.tname = tname;
}
public String getTcreater() {
	return tcreater;
}
public void setTcreater(String tcreater) {
	this.tcreater = tcreater;
}
public Date getTcreate_time() {
	return tcreate_time;
}
public void setTcreate_time(Date tcreate_time) {
	this.tcreate_time = tcreate_time;
}
@Override
public String toString() {
	return "TagItem [tag_id=" + tag_id + ", tname=" + tname + ", tcreater="
			+ tcreater + ", tcreate_time=" + tcreate_time + "]";
}



}
