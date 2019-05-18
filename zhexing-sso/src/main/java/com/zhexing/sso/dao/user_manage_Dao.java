package com.zhexing.sso.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhexing.sso.domain.report_detail;

/**
 * 后台管理Dao层
 * @author Administrator
 *
 */
public interface user_manage_Dao {
	//封停被举报用户
	void freezeUserByUser_id(Integer id);
	/**
	 * 根据uname查询该用户是否被冻结
	 * @return
	 */
	String isFreeze(String uname);
}
