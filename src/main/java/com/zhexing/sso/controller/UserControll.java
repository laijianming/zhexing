package com.zhexing.sso.controller;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sso.Seriver.Userseriver;
import com.zhexing.sso.Seriver.impl.UserServiceImpl;
import com.zhexing.sso.Util.VerificationCode;
import com.zhexing.sso.domain.User;
@RequestMapping("/register")
@RestController
public class UserControll {
	Userseriver userseriver=new UserServiceImpl();
	@RequestMapping("/check/{value}/{type}") //数据校验接口,具有回调功能
	@ResponseBody
	public ZheXingResult checkData(@PathVariable("value")String value,@PathVariable("type") String type,HttpSession session) throws Exception{
		if(!type.equals("uname")&&!type.equals("uemail")&&!type.equals("uphone")&&!type.equals("code")&&!type.equals("unewname")){//若数据类型不需要这三种之一，则验证失败
			return ZheXingResult.build(400, "数据验证类型错误");
		}
		if(!type.equals("code")){
		return userseriver.DataValidated(value, type);
		}
		else{
			String code=(String) session.getAttribute("code");
			if(!value.equals(code)){
				System.out.println(code);
				System.out.println("验证失败");
				return ZheXingResult.build(400, "验证码错误！");
			}else{
				return ZheXingResult.ok();
			}
		}
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public ZheXingResult createuser(User user){
		
		ZheXingResult result= userseriver.createUser(user);
		System.out.println("register:"+user);
		return result;
	}
	@RequestMapping("/token/{token}")
	@ResponseBody
	public ZheXingResult getUserByToken(@PathVariable("token")String token){
		return userseriver.getUserBuToken(token);
	}
	@RequestMapping("/activate")
	public void Activate(String token,HttpServletRequest request,HttpServletResponse response) throws Exception, IOException{
		ZheXingResult result= userseriver.activateUserByToken(token);
		System.out.println("激活:"+token);
		request.getServletContext().getRequestDispatcher("/success.html").forward(request, response);
	}
	 @RequestMapping("/code")
	    @ResponseBody
	    public ZheXingResult getCode(HttpServletRequest request,HttpServletResponse response,HttpSession session){
	    	try {
				String code=VerificationCode.getCode(request, response);
				session.setAttribute("code", code);
				return null;
	 		} catch (IOException e) {
				e.printStackTrace();
				return ZheXingResult.build(500,"验证码获取异常");
	 		}
	    }
	
	 
	
	 
	 
}
