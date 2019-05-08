package com.zhexing.sso.controller;

import java.io.File;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sso.Filter.FtpUtil;
import com.zhexing.sso.Filter.returnFilter;
import com.zhexing.sso.Mail.sendMail;
import com.zhexing.sso.Seriver.Userseriver;
import com.zhexing.sso.Seriver.impl.UserServiceImpl;
import com.zhexing.sso.domain.User;

@RestController
public class LoginContorll {
	Userseriver userseriver=new UserServiceImpl();
	@RequestMapping("/login")
	@ResponseBody
	public ZheXingResult Login( HttpServletRequest request){
		//获取请求中uname upassword 和自动登录标志
		String uname =request.getParameter("uname");
		String upassword=request.getParameter("upassword");
		String remember=request.getParameter("remember");
		//若密码或用户名为空 则返回提示信息
		if(uname==null||upassword==null){
			return ZheXingResult.build(500, "用户名密码不能为空！");
		}
		//调研service层进行登录验证
		ZheXingResult result=userseriver.userLogin(uname, upassword);
		//若状态码为200 则标记登录成功
		if(result.getStatus()==200){
			//通过msg来接受登陆时产生的令牌token
			String token=(String) result.getMsg();
			User user=userseriver.getUserByUP(uname, upassword);
			user.setToken(token);
			//进行用户自动登陆处理
			userseriver.RememberUser(user, remember);
		}
		return result;
	}
	 @RequestMapping("/loginOut")
	 @ResponseBody
	 public ZheXingResult LoginOut(String token){
		 //调用service服务注销该用户
		 ZheXingResult result=userseriver.Loginout(token);
		 return result;
	 }
	 @RequestMapping("/autoLogin")
	 @ResponseBody
	 public ZheXingResult AutoLogin(){
		 return null;
	 }
	 @RequestMapping("/uploadIcon")
	 @ResponseBody      /*public static boolean uploadFile(String host, int port, String username,
		String password, String basePath, String filePath, String filename,
		InputStream input) {*/
	 public ZheXingResult lll(HttpServletRequest request,HttpServletResponse response,MultipartFile file1){
//		 
//		 
//		  String uname=request.getParameter("uname");//获得文件上传的用户名
//		  User user=userseriver.findUserByUname(uname);
//		  if(user==null){
//			  return ZheXingResult.build(400,"用户未激活或用户名不存在！");
//		  }
//		  ZheXingResult result=userseriver.UpLoadIcon(request);//进行文件上传
//		  if(result.getStatus()==200){//状态码为200为上传成功！
//			  String path=(String) result.getData();//获得文件上传的本地路径
//			  System.out.println("path:"+path);
//			  path=path.replace("\\", "\\\\");  //将路径中/转换为// 方便文件下载时下载文件
//			  result=userseriver.SavePhotoPathByUname(uname,path);
////			  if(result.getStatus()==200){ //保存图片正常再执行头像下载
////				  System.out.println(""+path);
////				  response.setContentType("image/*;charset=UTF-8");//设定响应正文为jpg
////					result=userseriver.getIcon(uname,request,response);//进行文件下载
////					if(result.getStatus()==200){
////						return null;
////					}
////					return result;
////				}
//			  return result;
//			  }
//		  
//		  return result;
		  ZheXingResult result= userseriver.photoUpload(file1);
		 System.out.println(result.getData());
		 return result;
	 	}
	@RequestMapping("/getIcon")
	@ResponseBody
	public ZheXingResult getIcon(String uname,HttpServletRequest request,HttpServletResponse response){
		ZheXingResult result=null;
		response.setContentType("image/jpeg;charset=UTF-8");//设定响应正文为jpg
		result=userseriver.getIcon(uname,request,response);//进行文件下载
		if(result.getStatus()==200){
			return null;
		}
		return result;
	}
	@RequestMapping(value="/UpdateUserMessage")
	@ResponseBody
	public ZheXingResult updateUserMessage(String uname,String uemail,String unickname,String newuname,String uphone,String token){
			//作为邮箱是否被修改标志项
			boolean email=false;
			User user=new User();
			//当邮箱被修改时
		if(uemail!=null&&!"".equals(uemail.trim())){
			ZheXingResult result=userseriver.DataValidatedBesideLocal(uname, "uemail", uemail);
			if((boolean)result.getData()==false){
				return ZheXingResult.build(400, "该email已被占用！");
			}
			//标识邮箱被修改
			email=true; 
			user.setUemail(uemail);
		}
		//当用户名被修改时
		if(newuname!=null&&!"".equals(newuname.trim())){
			ZheXingResult result1=userseriver.DataValidatedBesideLocal(uname, "newuname", newuname);
			if((boolean)result1.getData()==false){
				return ZheXingResult.build(400, "该用户名已被占用！");
			}
			
		}
		//当用户手机被修改时
		if(uphone!=null&&!"".equals(uphone.trim())){
			ZheXingResult result1=userseriver.DataValidatedBesideLocal(uname, "uphone", uphone);
			if((boolean)result1.getData()==false){
				return ZheXingResult.build(400, "该手机号已被占用！");
			}
			user.setUphone(uphone);
		}
		if(unickname!=null&&!"".equals(unickname.trim())){
			
		user.setUnickname(unickname);
		}
		//调用service服务进行更新用户信息服务
		ZheXingResult result=userseriver.UpdateUserMessage(uname,user);
		System.out.println("eamil:"+email+" uemail:"+uemail);
		//当用户email被修改，切修改成功时，执行下面步骤
		if(email==true&&result.getStatus()==200){
			try {
				//设置user对象的uname
				if(newuname==null){
					newuname=uname;
				}
				user.setUname(newuname);
				//重置user用户的邮箱激活token
				ZheXingResult result2=userseriver.resetUserToken(user);
				//获得新的激活码
				user.setToken((String)result2.getData());
				//钝化该user用户，是ustatus为0
				userseriver.inactivationByUname(newuname);
				//发送邮件到新邮箱，提醒激活账号
				sendMail.Send(user);
				result.setMsg("亲爱的用户，由于你的邮箱进行了更改，系统已发送了一封激活邮件至宁的新邮箱 email:"+uemail+"  。请尽快前往邮箱激活账号吧！！！");
				userseriver.resetLoginStatusByToken(token);
			} catch (Exception e) {
				e.printStackTrace();
				return ZheXingResult.build(500,"账号钝化失败！");
			}
		}
		return result;
	}
	@RequestMapping(value="/checkPassword",method=RequestMethod.POST)
	@ResponseBody
	public ZheXingResult checkPasswork(HttpServletRequest request){
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
	@ResponseBody
	public ZheXingResult resetPassword(HttpServletRequest request){
		//获取请求参数
		String upassword=request.getParameter("upassword");
		String uname=request.getParameter("uname");
		if(uname==null||upassword==null||"".equals(upassword.trim())){
			return ZheXingResult.build(400, "注意用户名和新密码是否正确输入！");
		}
		//重设密码
		ZheXingResult result=userseriver.resetPasswordByUname(uname,upassword);
		return result;
	}
}
