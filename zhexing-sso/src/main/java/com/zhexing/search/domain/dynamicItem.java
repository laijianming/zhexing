package com.zhexing.search.domain;

import java.util.Date;
import java.util.List;

public class dynamicItem {
private Integer dynamic_id;
private long user_id;
private String user_uname;
private Date publish_time;
private String contents;
private String images;
private String user_unickname;
public Integer getDynamic_id() {
	return dynamic_id;
}
public void setDynamic_id(Integer dynamic_id) {
	this.dynamic_id = dynamic_id;
}
public Long getUser_id() {
	return user_id;
}
public void setUser_id(Long user_id) {
	this.user_id = user_id;
}
public Date getPublish_time() {
	return publish_time;
}
public void setPublish_time(Date publish_time) {
	this.publish_time = publish_time;
}


public String getContents() {
	return contents;
}
public void setContents(String contents) {
	this.contents = contents;
}

public String getImages() {
	return images;
}
public void setImages(String images) {
	this.images = images;
}
public String getUser_unickname() {
	return user_unickname;
}
public void setUser_unickname(String user_unickname) {
	this.user_unickname = user_unickname;
}
public String getUser_uname() {
	return user_uname;
}
public void setUser_uname(String user_uname) {
	this.user_uname = user_uname;
}
@Override
public String toString() {
	return "dynamicItem [dynamic_id=" + dynamic_id + ", user_id=" + user_id
			+ ", user_uname=" + user_uname + ", publish_time=" + publish_time
			+ ", contents=" + contents + ", images=" + images
			+ ", user_unickname=" + user_unickname + "]";
}




}
