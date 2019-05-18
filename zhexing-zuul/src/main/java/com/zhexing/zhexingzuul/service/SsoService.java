package com.zhexing.zhexingzuul.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.zhexing.common.pojo.User;
import com.zhexing.zhexingzuul.fallback.SocialFallback;
import com.zhexing.zhexingzuul.fallback.SsoFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zhexing.common.resultPojo.ZheXingResult;

@FeignClient(value = "ZHEXING-SSO",fallbackFactory = SsoFallback.class)
@Service
public interface SsoService {

	@RequestMapping("/hello")
	String hello();

	/**
	 * 用户注册方法
	 * @param user
	 * @return
	 */
	 @RequestMapping(value="/register/register", method=RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
	ZheXingResult createUser(User user);

	 /**根据标识token去redis查找该token是否过去或者是否已经登录
	 * 
	 * @param token token标志
	 * @return 若redis中存在该标志且没过期。则返回成功标志
	 * 若redis中不存在或者该标签已过期，返回失败标志
	 */
	@RequestMapping("/register/token/{token}")
	ZheXingResult getUserBuToken(@RequestParam("token") String token);
	
	/**
	 * 激活该标志的账号
	 * @param token
	 * @return
	 */
	@RequestMapping("/activate/{token}")
	ZheXingResult activateUserByToken(@RequestParam("token") String token);
	
	/**
	 * 注销
	 * @param token
	 * @return
	 */
	 @RequestMapping("/loginOut")
	ZheXingResult Loginout(@RequestParam("token") String token);

	/**
	 * 根据用户名和密码获取user对象。验证密码是否正确
	 * @param uname
	 * @param upassword
	 * @return
	 */
	@RequestMapping(value="/checkPassword",method=RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
	User finduserbyPU(@RequestParam("uname") String uname, @RequestParam("upassword") String upassword);

	/**
	 * 图片上传服务
	 * @param file1
	 * @return
	 */
	@PostMapping(value = "/uploadIcon", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ZheXingResult photoUpload(@RequestBody MultipartFile file1, @RequestParam("uname") String uname);
	/**
	 * 提供总的数据校验服务
	 * @param value
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/register/check/{value}/{type}")
	ZheXingResult checkData(@RequestParam("value") String value, @RequestParam("type") String type)throws Exception;
	/**
	 * 登录的总处理接口
	 * @return
	 */
	
//	@PostMapping(value = "/login")
	@RequestMapping(value = "/login" ,method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
	ZheXingResult login(User user);
	/**
	 * 处理更新用户信息总接口
	 * @param uname
	 * @param uemail
	 * @param unickname
	 * @param newuname
	 * @param uphone
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/UpdateUserMessage")
	ZheXingResult updateUserMessage(@RequestParam("uname") String uname, @RequestParam("uemail") String uemail, @RequestParam("unickname") String unickname, @RequestParam("newuname") String newuname, @RequestParam("uphone") String uphone, @RequestParam("token") String token);
    /**
     * 重置密码总接口
     * @return
     */
	@RequestMapping(value="/resetpassword",method=RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
	ZheXingResult resetPassword(User user);
}
