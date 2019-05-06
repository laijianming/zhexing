package com.zhexing.sociality.controller;

import com.zhexing.common.resultPojo.ZheXingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;

@RestController
public class FileController {


    /**
     * 上传图片
     * @param uploadFile
     * @return
     */
    @PostMapping("/social/upload/file")
    public ZheXingResult uploadFile(@RequestParam(value = "uploadFile", required = false)  MultipartFile uploadFile){
        System.out.println("上传文件 。。。 " + uploadFile.getOriginalFilename());
        return ZheXingResult.ok("http://192.168.2.123/zhexing/images/" + uploadFile.getOriginalFilename());
    }


    /**
     * 上传图片
     * @param image 图片的base64编码字符串
     * @return
     */
    @PostMapping("/social/upload/image64")
    public ZheXingResult uploadImage64(String image){
        System.out.println("上传图片 。。。 " + image);
//        GenerateImage(image)
        Long random = (long)(Math.random()*100000);
        String imgFilePath = "D:\\tupian\\" + random + ".jpg";//新生成的图片
        return ZheXingResult.ok(imgFilePath);
    }

    public static String GenerateImage(String imgStr)
    {   //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return "";
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片
            Long random = (long)(Math.random()*100000);
            String imgFilePath = "D:\\tupian\\" + random + ".jpg";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return imgFilePath;
        }
        catch (Exception e)
        {
            return "";
        }
    }


}
