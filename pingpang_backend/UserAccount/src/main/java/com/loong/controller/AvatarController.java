package com.loong.controller;

import com.loong.common.R;
import com.loong.util.FileUpUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;


@RestController
@RequestMapping("/avatar")
public class AvatarController {

    @Autowired
    @Qualifier("avatarPath")
    private String path;


    @PostMapping("/upload")
    public R upload(@RequestBody MultipartFile file) {
        String fileName = FileUpUtil.upFile(file, path);
        return R.success(fileName, "文件上传成功");
    }

    @GetMapping("/download")
    public R download(HttpServletResponse httpServletResponse,String filename) {
        boolean result = FileUpUtil.downFile(httpServletResponse, path, filename);
        if (result) {
            return R.success(null, "文件下载成功");
        }else{
            // 随机生成一张图片展示
            Random random = new Random();
            FileUpUtil.downFile(httpServletResponse, path, "default" + random.nextInt(5) + ".jpg");
            return R.success(null,"使用默认的图像");
        }
    }

}
