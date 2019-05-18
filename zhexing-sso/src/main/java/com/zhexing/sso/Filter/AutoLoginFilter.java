package com.zhexing.sso.Filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;
/**
 * 自动登录过滤器和未登录
 * @author Administrator
 *
 */
@Component
public class AutoLoginFilter implements HandlerInterceptor {

	private String redisIP = "129.204.212.52";
	
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Jedis jedis=new Jedis(redisIP, 6379);
		System.out.println("redisIp:"+redisIP);
		String token=request.getParameter("token");
		String IsLogin=jedis.get("REDIS_USER_SESSION_KEY:"+token);
		System.out.println("ISlogin:"+IsLogin+"  token:"+token);
		if(IsLogin==null){//若用户没有登录
			if(jedis.get(token+"_AutoLogin")!=null){//若选择了自动登陆，则将登陆信息存入redis
				System.out.println("AutoLogin:没有登录，选择了自动登录");
				jedis.set("REDIS_USER_SESSION_KEY:"+token, jedis.get(token+"_AutoLogin"));
				jedis.expire("REDIS_USER_SESSION_KEY:"+token, 1800);
				response.sendRedirect(request.getContextPath()+"/register/token/"+token);//重定向到根据token获取登录信息页面
				return false;
				}
				System.out.println("AutoLogin:没有登录，没有选择自动登录");
				response.sendRedirect(request.getContextPath()+"/register/token/"+token);//重定向到根据token获取登录信息页面
			return false;
		}else{ 
			response.sendRedirect(request.getContextPath()+"/register/token/"+token);
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
