package com.zhexing.sso.Util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VerificationCode {
public static String getCode(HttpServletRequest request,HttpServletResponse response) throws IOException{
	 //创造一副内存图片
	int width=120;
	int height=25;
	BufferedImage bi=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	//获得画笔
	Graphics g=bi.getGraphics();
	g.setColor(Color.BLUE);//将画笔设为蓝色
	g.drawRect(0, 0, width, height);//描边第一第二个参数确定左上角的坐标，第三 第四个坐标 确定描边的宽和高
	g.setColor(new Color(29,161,242));
	g.fillRect(1, 1, width-2, height-2);//填充背景色
	g.setColor(Color.red);
	Random r=new Random();
	for(int i=0;i<9;i++)
	g.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height));//两点确定一直线
	g.setColor(Color.white);
	g.setFont(new Font("Segoe UI",Font.ITALIC|Font.BOLD , 19));
	StringBuffer code=new StringBuffer();
	for(int y=0;y<4;y++){
		int n=r.nextInt(10);
		code.append(n);
		g.drawString(n+"", 20+18*y, 20);//画随机数字，第二个参数是距离左边框多少距离，第三个参数是距离上边框的距离
	}
//使用ImageIo输出 不能让浏览器缓存图片
	response.setHeader("Expires", "-1");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	OutputStream outputStream=response.getOutputStream();
	ImageIO.write(bi, "jpg",outputStream);
	return code.toString();
}
}
