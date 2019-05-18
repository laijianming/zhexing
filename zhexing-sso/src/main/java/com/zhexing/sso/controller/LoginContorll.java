package com.zhexing.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sso.Service.Userservice;
import com.zhexing.common.pojo.User;

@RestController
public class LoginContorll {

	@Autowired
	Userservice userseriver;

	@PostMapping("/login")
	public ZheXingResult Login(User user){
	ZheXingResult result=userseriver.login(user);
	return result;
	}

	 @RequestMapping("/loginOut")
	 public ZheXingResult LoginOut(String token){
		 //调用service服务注销该用户
		 ZheXingResult result=userseriver.Loginout(token);
		 return result;
	 }
	 
	 @RequestMapping("/autoLogin")
	 public ZheXingResult AutoLogin(){
		 return null;
	 }

	 @PostMapping("/uploadIcon")
	 public ZheXingResult lll(User user, HttpServletResponse response,@RequestBody MultipartFile file1){
		 String uname=user.getUname();
		 ZheXingResult result= userseriver.photoUpload(file1,uname);
		 System.out.println(result.getData());
		 return result;
	 	}
//	@RequestMapping("/getIcon")
//	public ZheXingResult getIcon(String uname, HttpServletRequest request,HttpServletResponse response){
//		ZheXingResult result=null;
//		response.setContentType("image/jpeg;charset=UTF-8");//设定响应正文为jpg
//		result=userseriver.getIcon(uname,request,response);//进行文件下载
//		if(result.getStatus()==200){
//			return null;
//		}
//		return result;
//	}
	
	@RequestMapping(value="/UpdateUserMessage")
	public ZheXingResult updateUserMessage(String uname,String uemail,String unickname,String newuname,String uphone,String token){
			ZheXingResult result=userseriver.updateUserMessage(uname, uemail, unickname, newuname, uphone, token);
			return result;
	}
	@RequestMapping(value="/checkPassword",method=RequestMethod.POST)
	public ZheXingResult checkPasswork(User user1){
		//获得请求参数
		String uname=user1.getUname();
		String upassword=user1.getUpassword();
		//调用服务查看该用户名和密码是否正确
		User user=userseriver.finduserbyPU(uname, upassword);
		if(user==null){
			return ZheXingResult.build(400,"密码错误！");
		}
		return ZheXingResult.build(200, "ok");
	}
	@RequestMapping(value="/resetpassword",method=RequestMethod.POST)
	public ZheXingResult resetPassword(User user){
		ZheXingResult result=userseriver.resetPassword(user);
		return result;
	}
}
