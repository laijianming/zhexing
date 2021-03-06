package com.zhexing.zhexingzuul.controller.zhexingSsoController;


import com.zhexing.common.pojo.User;
import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.zhexingzuul.service.SsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class CopyOfLoginControll {
	@Autowired
	SsoService userseriver;

	@PostMapping("/login")
	public ZheXingResult Login(User user){
		System.out.println(user);
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
	 public ZheXingResult lll(String uname, MultipartFile file1){
		 System.out.println(uname);
		 System.out.println(file1.getSize());
		ZheXingResult result= userseriver.photoUpload(file1,uname);
		 System.out.println(result.getData());
		 return result;
	 	}
	@RequestMapping("/getIcon")
	public ZheXingResult getIcon(String uname,HttpServletResponse response){
		ZheXingResult result=null;
		response.setContentType("image/jpeg;charset=UTF-8");//设定响应正文为jpg
		if(result.getStatus()==200){
			return null;
		}
		return result;
	}
	
	@RequestMapping(value="/UpdateUserMessage")
	public ZheXingResult updateUserMessage(String uname,String uemail,String unickname,String newname,String uphone){
			ZheXingResult result=userseriver.updateUserMessage(uname, uemail, unickname, newname, uphone);
			return result;
	}
	@RequestMapping(value="/checkPassword",method=RequestMethod.POST)
	public ZheXingResult checkPasswork( HttpServletRequest request){
		//获得请求参数
		String uname=request.getParameter("uname");
		String upassword=request.getParameter("upassword");
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
