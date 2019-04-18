package com.zhexing.sso.Filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhexing.sso.common.Myrequest;
/**
 * 处理请求参数(POST、GET)乱码和响应乱码过滤器
 * @author Administrator
 *
 */
public class MyCharacterEncodingFilter implements Filter {
private FilterConfig config=null;
	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.config=config;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		System.out.println("中文过滤器启动");
		HttpServletRequest request=null;
		HttpServletResponse response=null;
		try {
			request=(HttpServletRequest)req;
			response=(HttpServletResponse)res;
		} catch (Exception e) {
			throw new RuntimeException("not httpSevletRequest!!");
		}
		String encoding=config.getInitParameter("encoding");//获得在配置过滤器时设置的编码集	
		if(encoding==null){
			encoding="UTF-8";
		}
		request.setCharacterEncoding(encoding);//解决POT请求方法参数
		response.setContentType("text/html;charset="+encoding);
		HttpServletRequest request1=new Myrequest(request);//包装后的request解决了get请求乱码问题
		chain.doFilter(request1, response); //放行
	}

}
