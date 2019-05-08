package com.zhexing.sso.search.controll;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.search.domain.TagItem;
import com.zhexing.search.domain.TagResult;
import com.zhexing.search.domain.dynamicItem;
import com.zhexing.search.domain.dynamicResult;
import com.zhexing.search.service.SearchService;
import com.zhexing.search.service.impl.SearchServiceImpl;
import com.zhexing.sso.Util.DateJsonValueProcessor;

@RestController
@RequestMapping("/Search")
public class SearchContorll{
private SearchService searchService=new SearchServiceImpl();
	
@RequestMapping("/importAllTagAndDynamicInSolr")
@ResponseBody
public ZheXingResult importAllTagAndDynamicInSolr(){
	try {
		return searchService.importAllDyAndTag();
	} catch (Exception e) {
		e.printStackTrace();
		return ZheXingResult.build(500, e.getMessage());
	}
}
@RequestMapping(value="/queryDynamic",method=RequestMethod.GET)
@ResponseBody
public ZheXingResult query(@RequestParam("q")String queryString,@RequestParam(defaultValue="1")Integer page,@RequestParam(defaultValue="60")Integer rows){
	//查询条件不能为空
	if(queryString==null||"".equals(queryString.trim())){
		return ZheXingResult.build(400, "查询条件不能为空");
	}
	try {
		//调用服务查询动态
		dynamicResult result=searchService.searchDynamic(queryString, page, rows);
		//因为返回的dynamicResult中包含date对象，json默认对date类型的处理不符合，所以自定义了一个类型处理器
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject json =JSONObject.fromObject(result,jsonConfig);
		return ZheXingResult.ok(json);
	} catch (Exception e) {
		e.printStackTrace();
		return ZheXingResult.build(500, e.getMessage());
	}
	
}
@RequestMapping("/AddDynamic")
@ResponseBody
public ZheXingResult AddBynamic(HttpServletRequest request){
	try {
	//获取request中包含的请求参数
	Integer dynamic_id=Integer.parseInt(request.getParameter("dynamic_id"));
	Long user_id=Long.parseLong(request.getParameter("user_id"));
	String user_uname=request.getParameter("user_uname");
	DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date=format.parse(request.getParameter("publish_time"));
	String contents=request.getParameter("contents");
	String images=request.getParameter("images");
	String user_unickname=request.getParameter("user_unickname");
	//创建动态数据对象
	dynamicItem item=new dynamicItem();
	item.setDynamic_id(dynamic_id);
	item.setUser_id(user_id);
	item.setUser_uname(user_uname);
	item.setPublish_time(date);
	item.setImages(images);
	item.setUser_unickname(user_unickname);
	item.setContents(contents);
	//调用服务将新建的话题加入索引库
	ZheXingResult result=searchService.AddDynamic(item);
	return result;
	} catch (ParseException e) {
		e.printStackTrace();
		return ZheXingResult.build(500, e.getMessage());
	}
}
@RequestMapping("/DeleteDynamicDocById")
@ResponseBody
public ZheXingResult DeleteDynamicDocById(Integer id){
	try {
		//调用service层删除动态
		searchService.DeleteDynamicDocById(id);
		return ZheXingResult.ok();
	} catch (Exception e) {
		e.printStackTrace();
		return ZheXingResult.build(500,e.getMessage());
	}
}
@RequestMapping("/queryTag")
@ResponseBody
public ZheXingResult queryTag(@RequestParam("q")String queryString,@RequestParam(defaultValue="1")Integer page,@RequestParam(defaultValue="60")Integer rows){
	try {
		//调用searchService服务查询
		TagResult result=searchService.searchTag(queryString, page, rows);
		//创建jsonconfig对象
		JsonConfig jsonConfig=new JsonConfig();
		//注册json类型处理器
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject json=JSONObject.fromObject(result,jsonConfig);
		return ZheXingResult.ok(json);
	} catch (Exception e) {
		e.printStackTrace();
		return ZheXingResult.build(500,e.getMessage());
	}
}
@RequestMapping("/AddTag")
@ResponseBody
public ZheXingResult AddTag(HttpServletRequest request){
	try {
	//获取请求参数
	Integer tag_id=Integer.parseInt(request.getParameter("tag_id"));
	String tname=request.getParameter("tname");
	String tcreater=request.getParameter("tcreater");
	String tcreate_time=request.getParameter("tcreate_time");
	//获取时间解析器
	DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = format.parse(tcreate_time);
	//将请求数据填充入话题对象
	TagItem item=new TagItem();
	item.setTag_id(tag_id);
	item.setTcreate_time(date);
	item.setTname(tname);;
	item.setTcreater(tcreater);
	//调用service服务添加Tag话题
	searchService.AddTag(item);	
	return ZheXingResult.ok();
	} catch (Exception e) {
		e.printStackTrace();
		return ZheXingResult.build(500, e.getMessage());
	}
}
@RequestMapping("/DeleteTag")
@ResponseBody()
public ZheXingResult deleteTag(Integer id){
	try {
		searchService.DeleteTagById(id);
		return ZheXingResult.ok();
	} catch (Exception e) {
		e.printStackTrace();
		return ZheXingResult.build(500, e.getMessage());
	}
}
}
