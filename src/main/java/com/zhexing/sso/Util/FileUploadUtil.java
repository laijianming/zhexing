package com.zhexing.sso.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.sso.Filter.FtpUtil;

public class FileUploadUtil {
public static ZheXingResult Upload(MultipartFile MTFile,List<String> fileTypes,String parentPath){
	//获取上传文件的最简文件名
	String fileName=MTFile.getOriginalFilename();
	fileName =fileName.substring(fileName.lastIndexOf(File.separator)+1,fileName.length()); //截取出真正最简洁的文件名
	//为文件名添加上一串uuid让文件名重复时不会被覆盖
	String uID=UUID.randomUUID().toString(); //获取UUID防止上传图片重名而互相覆盖了
	
	//截取图片的扩展名
	String ext = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());  //截取出文件的扩展名
	ext=ext.toLowerCase();    //将文件的扩展名变小写
	fileName=uID+"_"+fileName; //结合uuid解决文件重名问题 
	
	//parentPath =parentPath.substring(parentPath.indexOf("/")+1,parentPath.lastIndexOf("/"));//将前面传过来的Maven工程下的springmvc.xml的父路径提取出来
	File file=null;
	if(fileTypes.contains(ext)){//若文件是fileTypes中标记的文件拓展类型才执行文件上传。
		//File parentFile=new File(parentPath); //建立父路径file对象
//		if(!parentFile.exists()){ //创建父路径文件夹
//			parentFile.mkdirs();
//		}
		String childPath=FileUploadUtil.getPath(fileName, parentPath);//获得更细化的子路径结构
		try {
			//MTFile.transferTo(new File(parentFile+""+File.separator+""+childPath+""+fileName));//文件上传！
			fileName =new String(fileName.getBytes("UTF-8"));
			boolean ok=FtpUtil.uploadFile("129.204.212.52", 21, "zhexing", "zhexing", parentPath,"", "3.jpg", MTFile.getInputStream());
			System.out.println("path:::::"+"www.gdouhz.cn"+parentPath+childPath+fileName);
			System.out.println(ok);
			return ZheXingResult.ok("www.gdouhz.cn/files"+childPath+"/"+fileName);//将成功上传时的文件存放路径存在zhexingresult的data区，方便在service层存入数据库
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			return ZheXingResult.build(500, e.getMessage());
		}
	}
	return ZheXingResult.build(400, "文件类型不允许！");
}
public static String getPath(String filename,String realpath){//这个方法是根据文件名来获取孩子目录的
	Date date=new Date();
	DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	String riqi=format.format(date);
	String x[]=riqi.split("-");
	String year=x[0]; //年份
	String month=x[1]; //月份 
	int dir1=filename.hashCode()&0xf;
	int dir2=(filename.hashCode()&0xf0)>>4;
	String childpath="/"+year+"/"+month+"/"+dir1+"/"+dir2;
	return childpath;
	
}

}
