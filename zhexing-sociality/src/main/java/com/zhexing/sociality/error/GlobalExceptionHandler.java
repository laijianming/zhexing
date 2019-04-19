package com.zhexing.sociality.error;

import com.zhexing.common.resultPojo.ZheXingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *  异常处理类
 *      处理整个系统的异常，将异常信息包装在ZhexingResult类中返回给前端做出响应
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ZheXingResult handleException(Exception e, HttpServletRequest request){
        System.out.println("GlobalExceptionHandler 启动 出现异常 封装新返回结果并响应");
        Map<String,Object> map = new HashMap<>();
        //传入我们自己的错误状态码 4xx 5xx，否则就不会进入定制错误页面的解析流程
        /**
         * Integer statusCode = (Integer) request
         * .getAttribute("javax.servlet.error.status_code");
         */
        map.put("code","server error");
        map.put("message",e.getMessage());
        // 告知前端 服务器出现错误
        return ZheXingResult.build(418,"server error",map);
    }

}
