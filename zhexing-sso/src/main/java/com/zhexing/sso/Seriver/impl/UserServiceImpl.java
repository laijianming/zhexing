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
	 
	
	private String redisIP = "192.168.2.123";
		
	@Override
	public ZheXingResult DataValidated(String value, String type) {
		SqlSession  session=SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		//使用dao层进行数据检验
		User user=userDao.datavalidated(value, type); 
		//若返回值不为空则说明该数据已经存在了，即数据不可用！
		if(user!=null){ 
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
			//为注册用户填充用户创建时间与更新时间
			user.setUpdated(new Date());
			user.setCreated(new Date());
			//为用户输入密码进行md5加密
			user.setUpassword(SecurityUtil.md5(user.getUpassword()));
			//生成用户唯一标识码.（用于邮箱激活）
			String token=UUID.randomUUID().toString(); 
			user.setToken(token);
			//调用dao添加用户
			userDao.addUser(user);
			//调用邮箱工具类发送激活邮箱
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
			//调用dao层进行登录判断
			User user=userDao.findUserByUsernameAndPassword(username, SecurityUtil.md5(password));
			//若返回的user为null，证明用户名或密码错误
			if(user==null){
				return ZheXingResult.build(400, "用户名或者密码错误");
			}
			//判断账号是否激活!
			if(userDao.isActional(user.getUname())==null){
				return ZheXingResult.build(400, "该账号未激活，请前往注册邮箱激活宁的账号！");
			}
			//登录验证通过后，生成令牌token,该令牌不同于激活邮箱的token，是用来存在redis里面标志登录状态的
			String token=UUID.randomUUID().toString();
			//将用户的密码清空后再存入redis，为了安全起见别把密码存入redis
			user.setUpassword(null);
			//获得redis客户端
			Jedis jedis=new Jedis(redisIP, 6379);
			//把令牌标记存入user对象中
			user.setToken(token);
			//创建jsonConfig对象，将一些前端不需要的字段不传回前端
			JsonConfig jsonConfig=new JsonConfig();
			jsonConfig.setExcludes(new String[]{"upassword","ustatus","created","updated"});
			//将登陆后的用户信息中的date类型的数据按自定义类型转换json
			jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));//注册json对象类型转换器 ,第一个参数是目标json类型,第二个是类型转换器
			JSONObject json=JSONObject.fromObject(user,jsonConfig);
			//将用户信息存入redis
			jedis.set("REDIS_USER_SESSION_KEY:"+token, json.toString());
			//设置session过期时间
			//jedis.expire("REDIS_USER_SESSION_KEY:"+token, 1800);
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
		Jedis jedis=new Jedis(redisIP, 6379);
		//根据令牌去查询该令牌是否登录了
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
		//调用dao层去激活账号
		userDao.actionUserByToken(token);
		session.commit();
		session.close();
		return ZheXingResult.ok();
	}

	@Override
	public void RememberUser(User user, String remember) {
		//获得redis客户端
		Jedis jedis=new Jedis(redisIP, 6379);
		user.setUpassword(null);
//		JsonConfig jsonConfig=new JsonConfig();
//		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));//注册json对象类型转换器 ,第一个参数是目标json类型,第二个是类型转换器
//		jsonConfig.setExcludes(new String[]{"upassword","uchathead","ustatus","created","updated","token","user_id"});
		JSONObject json=JSONObject.fromObject(user);
		//若点击了自动登录，将user的信息保存在redis中
		if("true".equals(remember)){
			System.out.println("自动登录工作！");
		jedis.set(user.getToken()+"_AutoLogin", json.toString());
		jedis.expire(user.getToken()+"_AutoLogin", 10*24*60*60);//该标记保存十天
		}
		//若没有点击自动登录，将redis中该用户原来的自动登陆的标志去掉
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
		//根据用户令牌，将redis中相关的登录标志去除 
		Jedis jedis=new Jedis(redisIP, 6379);
		jedis.del("REDIS_USER_SESSION_KEY:"+token);
			return ZheXingResult.ok();
	 }catch(Exception e){
		 return ZheXingResult.build(400, "注销失败!");
	 }
	 }

	@Override
	public ZheXingResult UpLoadIcon(HttpServletRequest request)  {
		try {
			//设置请求编码集，解决中文文件名乱码问题
			request.setCharacterEncoding("UTF-8");
			//检查表单类型，若不符合文件multipart/form-data则返回400状态码
			boolean isMulti=ServletFileUpload.isMultipartContent(request);
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
		//将头像路径保存入user数据库中
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
		//查询该数据除了自己在实验外，是否被其他人在占用
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
			//调用dao层对用户信息进行修改保存
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
		//当修改了邮箱后，使用dao层将用户状态置为冻结（0）
		userDao.inactivationUserByUname(uname);
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
			//调用dao层重设密码
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
		//生成新的激活token
		String token=UUID.randomUUID().toString();
		SqlSession session =SqlSessionUtil.getSqlSession();
		userDao userDao=session.getMapper(userDao.class);
		//调用dao重设用户激活标记
		userDao.resetUserToken(user.getUname(), token);
		session.commit();
		session.close();
		return ZheXingResult.ok(token);
	}

	@Override
	public ZheXingResult resetLoginStatusByToken(String token) {
		//获取redis客户端
		Jedis jedis=new Jedis(redisIP, 6379);
		//删除该用户的登录标记和记住登录标志
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
