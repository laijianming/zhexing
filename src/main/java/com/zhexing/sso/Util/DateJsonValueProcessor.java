package com.zhexing.sso.Util;

import java.text.SimpleDateFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
//将json中Date类型的数据以yyyy-MM-dd传给前端
public class DateJsonValueProcessor implements JsonValueProcessor {
	private String format;
	
	public DateJsonValueProcessor(String format){//实例化时传入希望转换成的类型
		this.format=format;
	}
	@Override
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {//这种value为数组date对象情况不处理
		return null;
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {//处理value为单个Date的情况
		if(value==null){//当value为空，直接返回null
			return "";
		}
		if(value instanceof java.sql.Timestamp){//当value是时间戳类型，将这个value强转为时间戳类型再按format格式输出时间字符串
			String str=new SimpleDateFormat(format).format((java.sql.Timestamp)value);
			return str;
		}
		if(value instanceof java.util.Date){//当value是时间类型，将这个value强转为时间类型再按format格式输出时间字符串
			String str=new SimpleDateFormat(format).format((java.util.Date)value);
			return str;
		}
		return value.toString();
	}

}
