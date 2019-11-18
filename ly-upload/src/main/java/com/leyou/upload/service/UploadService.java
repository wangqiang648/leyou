package com.leyou.upload.service;

import com.leyou.upload.controller.UploadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    //记录日志
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    //支持的文件类型
    private static final List<String> suffix = Arrays.asList("image/png", "image/jpeg");

    public String uploadImg(MultipartFile file) {
        try {
            //校验文件类型
            String type = file.getContentType();
            if(!suffix.contains(type)){
                //文件上传失败
                logger.info("文件类型不匹配", type);
                return null;
            }

            //校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null){
                //文件上传失败
                logger.info("文件内容不符合要求");
                return null;
            }

            //保存图片
            File dist = new File("D:\\heima\\upload");
            if(!dist.exists()){
                dist.mkdirs();
            }
            file.transferTo(new File(dist, file.getOriginalFilename()));

            //拼接图片地址
            String url = "http://image.leyou.com/upload/" + file.getOriginalFilename();
            return url;
        }catch (Exception e){
            return null;
        }
    }
}
