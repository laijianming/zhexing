package com.zhexing.sso.common;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
/**
 * request的包装类，修改了requestparam方法解决get请求参数乱码问题
 * @author Administrator
 *
 */
public class Myrequest extends HttpServletRequestWrapper{

	public Myrequest(HttpServletRequest request) {
		super(request);
	}
	@Override
	public String getParameter(String name) {
		if("get".equalsIgnoreCase(super.getMethod())){
			String value=super.getParameter(name);
			if(value==null||value.trim()==""){
				return value;
			}
			try {
				byte[] b=value.getBytes("ISO-8859-1");
				return new String(b,super.getCharacterEncoding());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
		return super.getParameter(name);
	}
	
	
}
