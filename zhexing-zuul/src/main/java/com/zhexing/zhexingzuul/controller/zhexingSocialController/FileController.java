package com.zhexing.zhexingzuul.controller.zhexingSocialController;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.zhexingzuul.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @Autowired
    SocialService socialService;


    /**
     * 上传图片
     * @param uploadFile
     * @return
     */
    @PostMapping("/social/upload/file")
    //@RequestParam(value = "uploadFile", required = false)
    public ZheXingResult uploadFile(@RequestPart MultipartFile uploadFile) {
        return socialService.uploadFile(uploadFile);
    }


    /**
     * 上传图片
     * @param image 图片的base64编码字符串
     * @return
     */
    @PostMapping("/social/upload/image64")
    public ZheXingResult uploadImage64(String image){
        return socialService.uploadImage64(image);
    }


}
