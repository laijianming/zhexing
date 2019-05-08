package com.zhexing.sso.domain;

import java.util.Date;

public class User {
private String user_id;
private String uname;
private String unickname;
private String upassword;
private String uchathead;//头像路径
private String uphone;
private String uemail;
private int ustatus;//用户状态码
private Date created;
private Date updated;
private String token;

public String getToken() {
	return token;
}
public void setToken(String token) {
	this.token = token;
}
public String getUpassword() {
	return upassword;
}
public void setUpassword(String upassword) {
	this.upassword = upassword;
}
public String getUser_id() {
	return user_id;
}
public void setUser_id(String user_id) {
	this.user_id = user_id;
}
public String getUname() {
	return uname;
}
public void setUname(String uname) {
	this.uname = uname;
}
public String getUnickname() {
	return unickname;
}
public void setUnickname(String unickname) {
	this.unickname = unickname;
}
public String getUchathead() {
	return uchathead;
}
public void setUchathead(String uchathead) {
	this.uchathead = uchathead;
}
public String getUphone() {
	return uphone;
}
public void setUphone(String uphone) {
	this.uphone = uphone;
}
public String getUemail() {
	return uemail;
}
public void setUemail(String uemail) {
	this.uemail = uemail;
}
public int getUstatus() {
	return ustatus;
}
public void setUstatus(int ustatus) {
	this.ustatus = ustatus;
}
public Date getCreated() {
	return created;
}
public void setCreated(Date created) {
	this.created = created;
}
public Date getUpdated() {
	return updated;
}
public void setUpdated(Date updated) {
	this.updated = updated;
}
@Override
public String toString() {
	return "User [user_id=" + user_id + ", uname=" + uname + ", unickname="
			+ unickname + ", uchathead=" + uchathead + ", uphone=" + uphone
			+ ", uemail=" + uemail + ", ustatus=" + ustatus + ", created="
			+ created + ", updated=" + updated + "]";
}



}
