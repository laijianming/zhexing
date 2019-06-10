package com.zhexing.zhexingzuul.fallback;

import com.zhexing.common.pojo.User;
import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.zhexingzuul.service.SsoService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SsoFallback implements FallbackFactory<SsoService> {
    
    
    @Override
    public SsoService create(Throwable throwable) {

        ZheXingResult result = ZheXingResult.build(418, "服务器内部错误！！Feign回调错误！！！");
        
        
        return new SsoService() {
            @Override
            public String hello() {
                return "hello error";
            }

            @Override
            public ZheXingResult createUser(User user) {
                return result;
            }

            @Override
            public ZheXingResult getUserBuToken(String token) {
                return result;
            }

            @Override
            public ZheXingResult activateUserByToken(String token) {
                return result;
            }

            @Override
            public ZheXingResult Loginout(String token) {
                return result;
            }

            @Override
            public User finduserbyPU(String uname, String upassword) {
                return null;
            }

            @Override
            public ZheXingResult photoUpload(MultipartFile file1, String uname) {
                return result;
            }

            @Override
            public ZheXingResult checkData(String value,String type) throws Exception {
                return result;
            }

            @Override
            public ZheXingResult login(User user) {
                return result;
            }

            @Override
            public ZheXingResult updateUserMessage(String uname, String uemail, String unickname, String newname, String uphone) {
                return result;
            }

            @Override
            public ZheXingResult resetPassword(User user) {
                return result;
            }
        };
    }
}
