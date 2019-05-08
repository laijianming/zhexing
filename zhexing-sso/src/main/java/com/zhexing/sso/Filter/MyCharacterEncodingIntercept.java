package com.zhexing.sso.Filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sun.net.httpserver.Filter.Chain;
import com.zhexing.sso.common.Myrequest;
/**解决全站中文乱码(POST GET)以及响应乱码问题 
 * @author Administrator
 *
 */
public class MyCharacterEncodingIntercept implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println("乱码拦截器启动！");
		request.setCharacterEncoding("UTF-8");//解决POST请求乱码
		response.setContentType("text/html;charset=UTF-8");//解决响应乱码问题
		HttpServletRequest request2=new Myrequest(request);	
		request=request2;
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
