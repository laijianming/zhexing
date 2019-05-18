package com.zhexing.sso.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
public class MyExceptiionHandler {
	@ExceptionHandler(value=Exception.class)
	public String handleException(Exception e,HttpServletRequest request){
		request.setAttribute("javax.servlet.error.status_code", "500");
		System.out.println("出错了");
		return "forward:/error";
	}
}
