package com.zhexing.sociality.controller;

import com.zhexing.common.resultPojo.ZheXingResult;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {


    @PostMapping("/social/upload/image")
    public ZheXingResult uploadFile(MultipartFile uploadFile){
        System.out.println("上传图片 。。。 " + uploadFile.getName());
        return ZheXingResult.ok("http://192.168.2.123/zhexing/images/" + uploadFile.getName());
    }


}
