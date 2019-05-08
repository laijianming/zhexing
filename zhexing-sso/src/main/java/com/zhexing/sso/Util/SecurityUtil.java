package com.zhexing.sso.Util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 密码加密 base64编码工具类
 * @author Administrator
 *
 */
public class SecurityUtil {
public static String md5(String string) throws Exception{
	MessageDigest md=MessageDigest.getInstance("md5");
	byte b[]=md.digest(string.getBytes());
	return SecurityUtil.Base64Encoder(new String(b));
}

public static String Base64Encoder(String s){
	BASE64Encoder base64Encoder=new BASE64Encoder();
	String result =base64Encoder.encode(s.getBytes());
	return result;
}

public static String Base64Decoder(String s) throws IOException{
	BASE64Decoder decoder=new BASE64Decoder();
	return new String(decoder.decodeBuffer(s));
}
}

