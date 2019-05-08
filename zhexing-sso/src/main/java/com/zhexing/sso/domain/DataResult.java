package com.zhexing.sso.domain;


public class DataResult {
private  Integer status;
private  String msg;
private  Object data;
public DataResult(){
	
}
public DataResult(Integer status, String msg, Object data) {
	super();
	this.status = status;
	this.msg = msg;
	this.data = data;
}

public static DataResult ok(){
	return new DataResult(200, "ok", null);
}

public static DataResult ok(Object data){
	return new DataResult(200, "ok", data);
}
public static DataResult build(Integer status,String msg){
	return new DataResult(status, msg, null);
}
}
