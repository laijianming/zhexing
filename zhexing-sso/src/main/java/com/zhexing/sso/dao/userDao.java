package com.zhexing.sso.dao;

import org.apache.ibatis.annotations.Param;

import com.zhexing.sso.domain.User;

public interface userDao {
/**
 * 对用户注册时每个数据的可行性进行检查
 * @param name要进行检查的字段
 * @param type字段的类型 1
 * @return
 */
User datavalidated(@Param("value")String value,@Param("type")String type);
/**
 * 查询该字段数据，别的用户是否进行使用了！
 * @param value要进行检查的字段
 * @param type 字段类型
 * @param uname 用户名
 * @return若被别的用户占用了。则返回使用该字段数据的用户。若没有被使用，则返回null
 */
User datavalidatedbesideLocal(@Param("value")String value,@Param("type")String type,@Param("uname")String uname);
/**
 * 向数据库添加一个用户
 * @param user
 */
void addUser(User user);
/**
 * 根据用户名和密码查询用户信息
 * @param username 用户名
 * @param password 密码
 * @return 若查询到该用户，返回用户信息，若找不到user对象，则返回null 
 */
User findUserByUsernameAndPassword(@Param("username")String username,@Param("password")String password);
/**
 * 根据标识符来激活账号
 * @param token
 */
void actionUserByToken(@Param("token")String token); 
/**
 * 根据用户名判断此用户是否激活
 * @param uname
 * @return若成功激活，返回该用户信息,若还处于冻结，返回null
 */
User isActional(String uname);
/**
 * 若remember为ture,则往redis添加该uname自动登陆标识，反则，去掉uname自动标识
 * @param uname
 * @param remember
 */
void remember(String token,String remember);
	/**
	 * 根据用户名查询出用户对象，若该用户名存在且状态码为1，返回对象，若用户不存在或者没激活，返回一个错误信息
	 * @param uname
	 * @return
	 */
User findUserByUname(String uname);
	/**
	 * 根据用户名更新用户头像路径
	 * @param uname
	 * @param path
	 */
void updatePhotePathByUname(@Param("uname")String uname,@Param("path") String path);
/**
 * 根据用户名更新该用户信息
 * @param uname 用户名
 * @param unewame 新用户名
 * @param email 新邮箱
 * @param uphone 新电话
 * @param unickname 新昵称
 */

void updateUserByUname(@Param("uname")String uname,@Param("unewname")String unewname,@Param("uemail")String email,@Param("uphone")String uphone,@Param("unickname")String unickname);

/**
 * 根据uname将账号ustatus置为0。钝化。
 * @param uname
 */
void inactivationUserByUname(String uname);
/**
 * 根据用户名更新密码
 * @param uname
 * @param upassword
 */
void resetPasswordByUname(@Param("uname")String uname,@Param("upassword")String upassword);
/**
 * 更新该用户的激活token
 * @param uname 用户名
 * @param token 激活标志
 */
void resetUserToken(@Param("uname")String uname,@Param("token")String token);


}
