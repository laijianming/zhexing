package com.zhexing.sso.Filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpRequest;

public class returnFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request=null;
		HttpServletResponse response=null;
		try{
			request=(HttpServletRequest) req;
			response=(HttpServletResponse) res;
		}catch(Exception e){
			throw new RuntimeException("not http service!");
		}
		System.out.println("插件安装完毕");
		response.setHeader("Access-Control-Allow-Origin",  "*");
		response.setContentType("application/json;charset=UTF-8");
		chain.doFilter(request, response);//放行
	}

}
