package com.zhexing.sso.Mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.zhexing.sso.domain.User;

/**
 * 给用户发送一封注册邮件
 * @author Administrator
 *
 */
public class sendMail {
public static void Send(User user) throws Exception{
	Properties props=new Properties();
	props.setProperty("mail.host", "smtp.qq.com");
	props.setProperty("mail.transport.protocol", "smtp");
	props.setProperty("mail.smtp.auth", "true");
	Session session=Session.getInstance(props);
	MimeMessage message=new MimeMessage(session);
	message.setFrom(new InternetAddress("1374413599@qq.com"));
	message.setRecipients(Message.RecipientType.TO, ""+user.getUemail());
	message.setSubject("[zhexing] Please verify your email address.");
	message.setContent("亲爱的"+user.getUnickname()+",感谢宁在[zhexing]注册账号<br/><h5>请猛戳下面的超链接以激活</h5><a href='http://192.168.1.114:8888/register/activate?token="+user.getToken()+"'>戳这里</a><br/>此邮件由系统自动发出，请勿回复信息！", "text/html;charset=UTF-8");
	message.saveChanges();
	Transport ts=session.getTransport();
	ts.connect("1374413599@qq.com", "liudgwdarlwjbaej");
	ts.sendMessage(message, message.getAllRecipients());
}
public static void main(String[] args) throws Exception {
	User user=new User();
	user.setToken("1234");
	user.setUnickname("钟强浩");
	user.setUemail("workzqh@163.com");
	sendMail.Send(user);
}
}
