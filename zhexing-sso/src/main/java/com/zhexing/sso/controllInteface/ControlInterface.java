package com.zhexing.sso.controllInteface;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sso.Filter.FtpUtil;
import com.zhexing.sso.Filter.returnFilter;
import com.zhexing.sso.Mail.sendMail;
import com.zhexing.sso.Service.Userservice;
import com.zhexing.sso.Service.impl.UserServiceImpl;
import com.zhexing.sso.domain.User;

@RestController
public interface ControlInterface {
	/**
	 * 用户登陆接口
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	 ZheXingResult Login(HttpServletRequest request);
	/**
	 * 用户注销接口
	 * @param token 用户登陆时产生的令牌token
	 * @return
	 */
	 @RequestMapping("/loginOut")
	 ZheXingResult LoginOut(String token);
	 /**
	  * 用户自动登陆接口
	  * @return
	  */
	 @RequestMapping("/autoLogin")
	 ZheXingResult AutoLogin();
	 /**
	  * 上传图片接口
	  * @param request
	  * @param response
	  * @param file1目标文件对象
	  * @return
	  */
	 @RequestMapping("/uploadIcon")
	 ZheXingResult lll(HttpServletRequest request, HttpServletResponse response, MultipartFile file1);
	 /**下载文件接口
	  * 
	  * @param uname 
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping("/getIcon")
	 ZheXingResult getIcon(String uname, HttpServletRequest request, HttpServletResponse response);
	 
	 /**
	  * 修改个人信息接口
	  * @param uname 用户名
	  * @param uemail 用户邮箱
	  * @param unickname 用户昵称
	  * @param newuname 用户新改的昵称
	  * @param uphone 用户手机
	  * @param token 用户令牌
	  * @return
	  */
	 @RequestMapping(value="/UpdateUserMessage")
	  ZheXingResult updateUserMessage(String uname, String uemail, String unickname, String newuname, String uphone, String token);
	 
	 /**
	  * 修改密码时，检查用户输入的原密码是否正确接口
	  * @param request
	  * @return
	  */
	 @RequestMapping(value="/checkPassword",method=RequestMethod.POST)
	  ZheXingResult checkPasswork(HttpServletRequest request);
	
	 /**
	  * 重置密码接口
	  * @param request
	  * @return
	  */
	 @RequestMapping(value="/resetpassword",method=RequestMethod.POST)
	  ZheXingResult resetPassword(HttpServletRequest request);
	 
	 /**
	  * 更新索引库接口：将目前所有的话题和动态更新至索引库
	  * @return
	  */
	 @RequestMapping("/importAllTagAndDynamicInSolr")
	  ZheXingResult importAllTagAndDynamicInSolr();
	 /**
	  * 搜索查询动态接口
	  * @param queryString
	  * @param page
	  * @param rows
	  * @return
	  */
	 @RequestMapping(value="/queryDynamic",method=RequestMethod.GET)
	  ZheXingResult query(@RequestParam("q") String queryString, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "60") Integer rows);
	 /**
	  * 添加动态至索引库接口
	  * @param request
	  * @return
	  */
	 @RequestMapping("/AddDynamic")
	  ZheXingResult AddBynamic(HttpServletRequest request);
	 /**
	  * 删除索引库动态接口
	  * @param id
	  * @return
	  */
	 @RequestMapping("/DeleteDynamicDocById")
	  ZheXingResult DeleteDynamicDocById(Integer id);
	 /**
	  * 搜索查询话题接口
	  * @param queryString 查询条件
	  * @param page 查询起始页数
	  * @param rows 查询行数
	  * @return
	  */
	 @RequestMapping("/queryTag")
	  ZheXingResult queryTag(@RequestParam("q") String queryString, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "60") Integer rows);
	 /**
	  * 添加话题至索引库接口
	  * @param request
	  * @return
	  */
	 @RequestMapping("/AddTag")
	  ZheXingResult AddTag(HttpServletRequest request);
	 /**
	  * 从索引库删除话题接口
	  * @param id 话题的id
	  * @return
	  */
	 @RequestMapping("/DeleteTag")
	  ZheXingResult deleteTag(Integer id);
	 /**
		 * 注册时，用户输入数据有效性检查
		 * @param value 要检查的数据值
		 * @param type 要检查的数据的类型
		 * @param session
		 * @return
		 * @throws Exception
		 */
		@RequestMapping("/check/{value}/{type}") //数据校验接口,具有回调功能
		 ZheXingResult checkData(@PathVariable("value") String value, @PathVariable("type") String type, HttpSession session) throws Exception;
		
		/**
		 * 用户注册接口
		 * @param user 用户对象
		 * @return
		 */
		@RequestMapping(value="/register", method=RequestMethod.POST)
		 ZheXingResult createuser(User user);
		
		/**
		 * 根据用户令牌判断用户的登陆状态接口
		 * @param token
		 * @return
		 */
		@RequestMapping("/token/{token}")
		 ZheXingResult getUserByToken(@PathVariable("token") String token);
		
		/**
		 * 用户激活账号接口
		 * @param token 用户带来的令牌
		 * @param request 
		 * @param response
		 * @throws Exception
		 * @throws IOException
		 */
		@RequestMapping("/activate")
		 void Activate(String token, HttpServletRequest request, HttpServletResponse response) throws Exception, IOException;
		 
			/**
			 * 验证码产生接口
			 * @param request
			 * @param response
			 * @param session
			 * @return
			 */
			@RequestMapping("/code")
		     ZheXingResult getCode(HttpServletRequest request, HttpServletResponse response, HttpSession session);
	 

}
