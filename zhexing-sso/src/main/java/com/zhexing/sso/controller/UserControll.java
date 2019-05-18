package com.zhexing.sso.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sso.domain.report;
import com.zhexing.sso.Service.Userservice;
import com.zhexing.sso.Util.VerificationCode;
import com.zhexing.common.pojo.User;
@RequestMapping("/register")

@RestController
public class UserControll {
	@Autowired
	Userservice userseriver;
	
	@RequestMapping("/check/{value}/{type}") //数据校验接口,具有回调功能
	@ResponseBody
	public ZheXingResult checkData(@PathVariable("value")String value,@PathVariable("type") String type,HttpSession session) throws Exception{
		ZheXingResult result=userseriver.checkData(value, type, session);
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
	public void Activate(@PathVariable("token")String token,HttpServletRequest request,HttpServletResponse response) throws Exception, IOException{
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
	 
	 @RequestMapping("/report")
	public ZheXingResult Report(String reporter_id,String reported_id,Integer report_type, String report_content ){
		report report=new report();
		report.setReport_content(report_content);
		report.setReport_type(report_type);
		report.setReported_id(Long.parseLong(reported_id));
		report.setReported_time(new Date());
		report.setReporter_id(Long.parseLong(reporter_id));
		ZheXingResult result=userseriver.Report(report);
		return result;
	}
	 
}
