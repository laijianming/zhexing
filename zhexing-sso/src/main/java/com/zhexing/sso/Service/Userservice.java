package com.zhexing.sso.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zhexing.common.pojo.User;
import org.springframework.web.multipart.MultipartFile;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sso.domain.report;

public interface Userservice {
	/**
	 * 调用userDao进行数据的验证
	 * @param value数据值
	 * @param type数据字段名
	 * @return若该数据有效则返回数据正常的dataResult,若数据无效则返回数据无效的dataResult
	 */
	 ZheXingResult DataValidated(String value, String type);
	 
	 
	 /**
	  * userDao进行数据的验证
	  * @param uname 进行数据修改的用户名
	  * @param type 数据字段名
	  * @param value 数据值
	  * @return 若该数据有效则返回数据正常的dataResult,若数据无效则返回数据无效的dataResult
	  */
	 ZheXingResult DataValidatedBesideLocal(String uname, String type, String value);
	/**
	 * 用户注册方法
	 * @param user
	 * @return
	 */
	ZheXingResult createUser(User user);
	/**
	 * 用户登录方法
	 * @param username
	 * @param password
	 * @return
	 */
	ZheXingResult userLogin(String username, String password);
	/**根据标识token去redis查找该token是否过去或者是否已经登录
	 * 
	 * @param token token标志
	 * @return 若redis中存在该标志且没过期。则返回成功标志
	 * 若redis中不存在或者该标签已过期，返回失败标志
	 */
	ZheXingResult getUserBuToken(String token);
	/**
	 * 激活该标志的账号
	 * @param token
	 * @return
	 */
	ZheXingResult activateUserByToken(String token);
	/**
	 * 将这个uname记录到redis中，作为自动登陆的标记
	 * 若remember为true 则往redis中添加自动登陆标记，若为false去掉这个自动登陆标记
	 */
	void RememberUser(User user, String remember);
	/**
	 * 根据用户名和密码返回user
	 * @param uname
	 * @param upassword
	 * @return
	 */
	User getUserByUP(String uname, String upassword);
	/**
	 * 注销
	 * @param token
	 * @return
	 */
	ZheXingResult Loginout(String token);
	/**
	 * 上传用户头像
	 * @return
	 */
	ZheXingResult UpLoadIcon(HttpServletRequest request) ;
	/**根据用户名返回用户的信息
	 * 
	 * @param uname用户名
	 * @return若用户存在且激活了，返回该用户信息，若该用户不存在，或未激活，返回null对象
	 */
	User findUserByUname(String uname);
	/**
	 * 
	 * 根据用户名保存
	 * @param uname
	 * @return
	 */
	ZheXingResult SavePhotoPathByUname(String uname, String path);
	/**
	 * 根据用户名下载头像
	 * @param uname
	 * @return
	 */
	ZheXingResult getIcon(String uname, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 根据用户名去更新用户信息
	 * @param uname
	 * @param user
	 * @return
	 */
	ZheXingResult UpdateUserMessage(String uname, User user);
	/**
	 * 根据用户名钝化。使用该用户的status=0
	 * @param uname 用户名
	 * @return
	 */
	ZheXingResult inactivationByUname(String uname);
	/**
	 * 根据用户名和密码获取user对象。验证密码是否正确
	 * @param uname
	 * @param upassword
	 * @return
	 */
	User finduserbyPU(String uname, String upassword);

	/**
	 * 根据用户名更新密码
	 * @param uname
	 * @param upassword
	 * @return
	 */
	ZheXingResult resetPasswordByUname(String uname, String upassword);
/**
 * 重置该用户的激活token	
 * @param user 用户名
 * @return 返回新的激活码
 */
	ZheXingResult resetUserToken(User user);
	/**
	 * 根据令牌删除用户记住登录标志，删除在线标记
	 * @param token
	 * @return
	 */
	ZheXingResult resetLoginStatusByToken(String token);
	/**
	 * 图片上传服务
	 * @param file1
	 * @return
	 */
	ZheXingResult photoUpload(MultipartFile file1, String uname);
	/**
	 * 提供总的数据校验服务
	 * @param value
	 * @param type
	 * @param session
	 * @return
	 * @throws Exception
	 */
	ZheXingResult checkData(String value, String type, HttpSession session)throws Exception;
	/**
	 * 登录的总处理接口
	 * @param request
	 * @return
	 */
	
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
	ZheXingResult updateUserMessage(String uname, String uemail, String unickname, String newuname, String uphone, String token);
    /**
     * 重置密码总接口
     * @param request
     * @return
     */
	ZheXingResult resetPassword(User user);
	/**
	 * 举报用户接口
	 * @param report
	 * @return
	 */
	ZheXingResult Report(report report);
}
