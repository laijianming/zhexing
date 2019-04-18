package com.zhexing.sso.Seriver.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.RequestContext;

import redis.clients.jedis.Jedis;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sso.Filter.FtpUtil;
import com.zhexing.sso.Filter.returnFilter;
import com.zhexing.sso.Mail.sendMail;
import com.zhexing.sso.Seriver.Userseriver;
import com.zhexing.sso.Util.DateJsonValueProcessor;
import com.zhexing.sso.Util.FileUploadUtil;
import com.zhexing.sso.Util.SecurityUtil;
import com.zhexing.sso.Util.SqlSessionUtil;
import com.zhexing.sso.dao.userDao;
import com.zhexing.sso.domain.User;

public class UserServiceImpl implements Userseriver {
	 
		
	@Override
	public ZheXingResult DataValidated(String value, String type) {
		SqlSession  session=SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		User user=userDao.datavalidated(value, type); //使用dao层进行数据检验
		if(user!=null){ //若返回值不为则说明该数据已经存在了
			System.out.println(user);
			return ZheXingResult.ok(false);
		}
		session.commit();
		session.close();
		return ZheXingResult.ok(true);
	}

	@Override
	public ZheXingResult createUser(User user) {
		try {
			SqlSession  session=SqlSessionUtil.getSqlSession();
			userDao userDao=session.getMapper(userDao.class);
			user.setUpdated(new Date());
			user.setCreated(new Date());
			System.out.println("password:"+SecurityUtil.md5(user.getUpassword()));
			user.setUpassword(SecurityUtil.md5(user.getUpassword()));
			String token=UUID.randomUUID().toString(); //生成用户唯一标识码.
			user.setToken(token);
			userDao.addUser(user);
			sendMail.Send(user);
			session.commit();
			session.close();
			return ZheXingResult.build(200, "尊重的["+user.getUnickname()+"],您的账号已成功注册,请打开你的邮箱:"+user.getUemail()+" 去激活你的账号吧");
		} catch (Exception e) {
			e.printStackTrace();
			
			return ZheXingResult.build(500,e.getMessage());
		}
	}

	@Override
	public ZheXingResult userLogin(String username, String password) {
		SqlSession  session=SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		try {
			User user=userDao.findUserByUsernameAndPassword(username, SecurityUtil.md5(password));
			if(user==null){
				return ZheXingResult.build(400, "用户名或者密码错误");
			}
			if(userDao.isActional(user.getUname())==null){
				return ZheXingResult.build(400, "该账号未激活，请前往注册邮箱激活宁的账号！");
			}
			String token=UUID.randomUUID().toString();//登录验证通过后，生成token
			//将用户的密码清空后再存入redis
			System.out.println("login_Token:"+token);
			user.setUpassword(null);
			Jedis jedis=new Jedis("192.168.1.126", 6379);
			user.setToken(token);
			JsonConfig jsonConfig=new JsonConfig();
			jsonConfig.setExcludes(new String[]{"upassword","uchathead","ustatus","created","updated","user_id"});
			jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));//注册json对象类型转换器 ,第一个参数是目标json类型,第二个是类型转换器
			JSONObject json=JSONObject.fromObject(user,jsonConfig);
			System.out.println("json:"+json);
			//将用户信息存入redis
			jedis.set("REDIS_USER_SESSION_KEY:"+token, json.toString());
			//设置session过期时间
			jedis.expire("REDIS_USER_SESSION_KEY:"+token, 1800);
			//返回token
			session.commit();
			session.close();
			return ZheXingResult.build(200, token,json.toString());
		} catch (Exception e) {
			session.commit();
			session.close();
			e.printStackTrace();
			return ZheXingResult.build(500,e.getMessage());
		}
	}

	@Override
	public ZheXingResult getUserBuToken(String token) {
		Jedis jedis=new Jedis("192.168.1.126", 6379);
		System.out.println("用户token:"+token);
		String json=jedis.get("REDIS_USER_SESSION_KEY:"+token);
		//若获取到了空字符串，则该标记已无效了
		if("".equals(json)||json==null){
			return ZheXingResult.build(400, "此session已过期，请重新登录");
		}
		//更新该标志的过期时间。
		jedis.expire("REDIS_USER_SESSION_KEY:"+token, 1800);
		
		return ZheXingResult.ok(json);
	}

	@Override
	public ZheXingResult activateUserByToken(String token) {
		SqlSession  session=SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		userDao.actionUserByToken(token);
		session.commit();
		session.close();
		return ZheXingResult.ok();
	}

	@Override
	public void RememberUser(User user, String remember) {
		Jedis jedis=new Jedis("192.168.1.126", 6379);
		user.setUpassword(null);
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));//注册json对象类型转换器 ,第一个参数是目标json类型,第二个是类型转换器
		jsonConfig.setExcludes(new String[]{"upassword","uchathead","ustatus","created","updated","token","user_id"});
		JSONObject json=JSONObject.fromObject(user,jsonConfig);
		if("true".equals(remember)){//若点击了自动登录
			System.out.println("自动登录工作！");
		jedis.set(user.getToken()+"_AutoLogin", json.toString());
		jedis.expire(user.getToken()+"_AutoLogin", 10*24*60*60);//该标记保存十天
		}
		else {
		if(jedis.get(user.getToken()+"_AutoLogin")!=null){//若本次登陆未点击自动登录，则去redis取消自动登陆标志
			jedis.del(user.getToken()+"_AutoLogin");
		}
		}
	}



	@Override
	public User getUserByUP(String uname, String upassword) {
		SqlSession  session=SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		try {
			User user=userDao.findUserByUsernameAndPassword(uname, SecurityUtil.md5(upassword));
			session.commit();
			session.close();
			return user;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	@Override
	public ZheXingResult Loginout(String token) {
	 try{
		Jedis jedis=new Jedis("192.168.1.126", 6379);
		jedis.del("REDIS_USER_SESSION_KEY:"+token);
			return ZheXingResult.ok();
	 }catch(Exception e){
		 return ZheXingResult.build(400, "注销失败!");
	 }
	 }

	@Override
	public ZheXingResult UpLoadIcon(HttpServletRequest request)  {
		try {
			
			request.setCharacterEncoding("UTF-8");//设置请求编码集，解决中文文件名乱码问题
			boolean isMulti=ServletFileUpload.isMultipartContent(request);//检查表单类型，若不符合文件multipart/form-data则返回400状态码
			if(!isMulti){
				return ZheXingResult.build(400, "请检查你表单提交类型！");
			}
			MultipartHttpServletRequest mRequest=(MultipartHttpServletRequest) request;//获取multipartHttpservletrequest对象，文件信息在这个对象里面
			MultipartFile file1=mRequest.getFile("file1");//提取表单中属性名为file1的文件
			//String realPath=Resources.getResourceURL("springmvc.xml").toString();//这个路径的话是在Maven工程对应的java/main/resource/下的路径
			List<String> fileTypes=new ArrayList<String>();//这个是一个自定义的文件类型集合，可以进行文件的类型的过滤
			fileTypes.add("jpg");
			fileTypes.add("jpeg");
			fileTypes.add("bmp");
			fileTypes.add("gif");
			fileTypes.add("png");
			ZheXingResult result= null;
			if(!(file1.getOriginalFilename()==null||"".equals(file1.getOriginalFilename()))) //若文件名不为空时执行文件上传
			{
				result= FileUploadUtil.Upload(file1, fileTypes, "/home/img/www/files");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return ZheXingResult.build(400, "上传失败！");
		}
		
		
		
	}

	@Override
	public User findUserByUname(String uname) {
		SqlSession  session=SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		User user=userDao.findUserByUname(uname);
		session.commit();
		session.close();
		if(user!=null){
			return user;
		}
		
		return null;
	}

	@Override
	public ZheXingResult SavePhotoPathByUname(String uname, String path) {
		try{
		SqlSession  session=SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		userDao.updatePhotePathByUname(uname,path);
		session.commit();
		session.close();
		return ZheXingResult.ok(path);
		}catch(Exception e){
			e.printStackTrace();
			return ZheXingResult.build(500, e.getMessage());
		}
	}

	@Override
	public ZheXingResult getIcon(String uname,HttpServletRequest request,HttpServletResponse response) {
		SqlSession  session=SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		User user=userDao.findUserByUname(uname);
		if(user==null){
			return ZheXingResult.build(400, "该用户不存在或没激活账号!");
		}
		try {
			InputStream in=new FileInputStream(user.getUchathead());
			byte b[]=new byte[1024];
			int len=-1;
	        OutputStream out=response.getOutputStream();
	        while((len=in.read(b))!=-1){
	        	out.write(b, 0, len);
	        }
	        in.close();
	        out.flush();out.close();
	        session.commit();
	        session.close();
	        return ZheXingResult.ok();
		} catch (Exception e) {
	        session.commit();
	        session.close();
			e.printStackTrace();
			return ZheXingResult.build(500, "头像获取失败！");
		}
	}

	@Override
	public ZheXingResult DataValidatedBesideLocal(String uname, String type,
			String value) {
		SqlSession  session=SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		User user=userDao.datavalidatedbesideLocal(value, type, uname);
		session.commit();
        session.close();
		if(user!=null){
			return ZheXingResult.ok(false);
		}
		
		return ZheXingResult.ok(true);
	}

	@Override
	public ZheXingResult UpdateUserMessage(String uname, User user) {
		if(uname==null){
			return ZheXingResult.build(400,"uname不能为空！");
		}
		SqlSession  session=null;
		try{
			session =SqlSessionUtil.getSqlSession();
			userDao userDao=session.getMapper(userDao.class);
			userDao.updateUserByUname(uname, user.getUname(), user.getUemail(), user.getUphone(), user.getUnickname());
			session.commit();
	        session.close();
	        return ZheXingResult.ok(true);
		}catch(Exception e){
			e.printStackTrace();
			session.commit();
	        session.close();
			return ZheXingResult.build(500, e.getMessage());
		}
	}

	@Override
	public ZheXingResult inactivationByUname(String uname) {
		SqlSession session =SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		
		userDao.inactivationUserByUname(uname);
		System.out.println("钝化："+uname);
		session.commit();
        session.close();
		return ZheXingResult.ok();
	}

	@Override
	public User finduserbyPU(String uname, String upassword) {
		SqlSession session =SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		User user=userDao.findUserByUsernameAndPassword(uname, upassword);
		session.commit();
        session.close();
		return user;
	}

	@Override
	public ZheXingResult resetPasswordByUname(String uname, String upassword) {
		SqlSession session =SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		try {
			userDao.resetPasswordByUname(uname, SecurityUtil.md5(upassword));
			return ZheXingResult.ok();
		} catch (Exception e) {
			session.commit();
			session.close();
			e.printStackTrace();
			return ZheXingResult.build(500, e.getMessage());
		}
		
	}

	@Override
	public ZheXingResult resetUserToken(User user) {
		String token=UUID.randomUUID().toString();
		SqlSession session =SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		userDao.resetUserToken(user.getUname(), token);
		session.commit();
		session.close();
		return ZheXingResult.ok(token);
	}

	@Override
	public ZheXingResult resetLoginStatusByToken(String token) {
		Jedis jedis=new Jedis("192.168.1.126", 6379);
		jedis.del("REDIS_USER_SESSION_KEY:"+token);
		jedis.del(token+"_AutoLogin");
			return ZheXingResult.ok();
	}

	@Override
	public ZheXingResult photoUpload(MultipartFile file1) {
		// 生成一个新的文件名
					// 取原始文件名
					String oldName = file1.getOriginalFilename();
					// 生成新文件名
					// UUID.randomUUID();
					String newName = UUID.randomUUID().toString();
					newName = newName + oldName.substring(oldName.lastIndexOf("."));
					// 图片上传
					//获得上传日期
					Date date=new Date();
					DateFormat format=new SimpleDateFormat("/yyyy/MM/dd");
					String imgPath=format.format(date);
					try {
						FtpUtil.uploadFile("www.gdouhz.cn", 21, "zhexing", "zhexing", "/home/img/www/files", imgPath, newName, file1.getInputStream());
						return ZheXingResult.ok("129.204.212.52/files"+imgPath+"/"+newName);
					} catch (IOException e) {
						e.printStackTrace();
						return ZheXingResult.build(500, e.getMessage());
					}
	}
}
