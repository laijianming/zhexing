package com.zhexing.sso.controllInteface.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sso.controllInteface.ControlInterface;
import com.zhexing.sso.domain.User;

public class ControllImpl implements ControlInterface {
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Override
	public ZheXingResult Login(HttpServletRequest request) {
		
		return ZheXingResult.build(500,"错误的用户名或密码");
	}

	@Override
	public ZheXingResult LoginOut(String token) {
		
		return ZheXingResult.build(500,"注销失败");
	}

	@Override
	public ZheXingResult AutoLogin() {
		
		return ZheXingResult.build(500,"自动登陆失败！");
	}

	@Override
	public ZheXingResult lll(HttpServletRequest request,
			HttpServletResponse response, MultipartFile file1) {
		
		return ZheXingResult.build(500,"文件上传失败！");
	}

	@Override
	public ZheXingResult getIcon(String uname, HttpServletRequest request,
			HttpServletResponse response) {
		return ZheXingResult.build(500, "文件下载失败");
	}

	@Override
	public ZheXingResult updateUserMessage(String uname, String uemail,
			String unickname, String newuname, String uphone, String token) {
		
		return ZheXingResult.build(500,"用户更新资料失败!");
	}

	@Override
	public ZheXingResult checkPasswork(HttpServletRequest request) {
		
		return ZheXingResult.build(500,"重置密码失败！");
	}

	@Override
	public ZheXingResult resetPassword(HttpServletRequest request) {
		
		return ZheXingResult.build(500, "重置密码失败！");
	}

	@Override
	public ZheXingResult importAllTagAndDynamicInSolr() {
		return ZheXingResult.build(500, "更新动态、话题索引库失败");
	}

	@Override
	public ZheXingResult query(String queryString, Integer page, Integer rows) {
		return ZheXingResult.build(500,"搜索失败！");
	}

	@Override
	public ZheXingResult AddBynamic(HttpServletRequest request) {
		return ZheXingResult.build(500,"添加动态信息失败");
	}

	@Override
	public ZheXingResult DeleteDynamicDocById(Integer id) {
		return ZheXingResult.build(500,"从solr索引库删除动态记录失败");
	}

	@Override
	public ZheXingResult queryTag(String queryString, Integer page, Integer rows) {
		return ZheXingResult.build(500, "从索引库查询话题记录失败！");
	}

	@Override
	public ZheXingResult AddTag(HttpServletRequest request) {
		return ZheXingResult.build(500,"添加话题到solr索引库失败！");
	}

	@Override
	public ZheXingResult deleteTag(Integer id) {
		return ZheXingResult.build(500, "从solr索引库删除话题记录失败");
	}

	@Override
	public ZheXingResult checkData(String value, String type,
			HttpSession session) throws Exception {
		return ZheXingResult.build(500, "数据可行性校验异常");
	}

	@Override
	public ZheXingResult createuser(User user) {
		return ZheXingResult.build(500,"注册用户失败！");
	}

	@Override
	public ZheXingResult getUserByToken(String token) {
		return ZheXingResult.build(500,"登陆状态判断失败");
	}

	@Override
	public void Activate(String token, HttpServletRequest request,
			HttpServletResponse response) throws Exception, IOException {
		
	}

	@Override
	public ZheXingResult getCode(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		return ZheXingResult.build(500,"获得验证码失败！");
	}

}
