package com.zhexing.zhexingzuul.controller.zhexingSsoController;

import com.zhexing.common.pojo.User;
import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.zhexingzuul.service.SsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequestMapping("/register")
@RestController
public class CopyOfUserControll {
	@Autowired
	SsoService userseriver;
	
	@RequestMapping("/check/{value}/{type}") //数据校验接口,具有回调功能
	@ResponseBody
	public ZheXingResult checkData(@PathVariable("value")String value, @PathVariable("type") String type) throws Exception{
		ZheXingResult result=userseriver.checkData(value,type);
	return result;
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public ZheXingResult createuser( User user){
		
		ZheXingResult result= userseriver.createUser(user);
		System.out.println("register:"+user);
		return result;
	}
	@RequestMapping("/token/{token}")
	@ResponseBody
	public ZheXingResult getUserByToken(@PathVariable("token")String token){
		return userseriver.getUserBuToken(token);
	}
	
	@RequestMapping("/activate/{token}")
	public void Activate(@PathVariable("token")String token, HttpServletResponse response) throws Exception, IOException {
		ZheXingResult result= userseriver.activateUserByToken(token);
		System.out.println("激活:"+token);
	}
	 

}
