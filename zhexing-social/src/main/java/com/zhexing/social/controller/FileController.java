package com.zhexing.social.controller;

import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.social.utils.FtpUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FileController {


    /**
     * 上传图片
     * @param uploadFile
     * @return
     */
    @PostMapping("/social/upload/file")
    public ZheXingResult uploadFile(@RequestParam(value = "uploadFile", required = false)  MultipartFile uploadFile) throws IOException {
        System.out.println("上传文件 。。。 " + uploadFile.getOriginalFilename());
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String key = df.format(new Date());
        String imagePath = "/" + key  + "/image" ;
        String newName = new SimpleDateFormat("mmss").format(new Date()) + uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf("."),uploadFile.getOriginalFilename().length());
        FtpUtil.uploadFile("129.204.212.52",21,"xuzeqin","xuzeqin","/home/zhexing/images",imagePath,newName,uploadFile.getInputStream());
        String image = "http://129.204.212.52/images" + imagePath + "/"  + newName;
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("images",image);


        // 压缩图片并上传
        // 1、创建一个ByteArrayOutputStream字节数组用来保存
        ByteArrayOutputStream byteArrayOutputStreamut = new ByteArrayOutputStream();
        // 2、使用谷歌的开源工具进行压缩  scale 比例；outputQuality 质量； toxxx 将压缩过后的数据传到的位置
        Thumbnails.of(uploadFile.getInputStream()).scale(0.2f).outputQuality(0.5f).toOutputStream(byteArrayOutputStreamut);
        // 3、将上面得到的字节输出流转换成字节输入流，以便ftp上传文件
        ByteArrayInputStream b = new ByteArrayInputStream(byteArrayOutputStreamut.toByteArray());
        // 生成图片名
        String mimagePath = "/" + key  + "/mimage";
        // 上传文件
        FtpUtil.uploadFile("129.204.212.52",21,"xuzeqin","xuzeqin","/home/zhexing/images",mimagePath,newName,b);
        String mimage = "http://129.204.212.52/images" + mimagePath + "/"  + newName;
        resultMap.put("mimages",mimage);

        // 写文件
//        DataOutputStream o = new DataOutputStream(new FileOutputStream(new File("D:\\" + newName)));
//        o.write(byteArrayOutputStreamut.toByteArray());
//        o.close();
//        FtpUtil.uploadFile("129.204.212.52",21,"zhexing","zhexing","/home/zhexing/images",imagePath,newName,mimgfile.get);

        return ZheXingResult.ok(resultMap);
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
